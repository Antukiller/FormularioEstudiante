package dev.antoine.formularioestudiante.estudiante.models.ViewModel

import com.github.michaelbull.result.*
import dev.antoine.formularioestudiante.estudiante.models.Estudiante
import dev.antoine.formularioestudiante.estudiante.models.errors.EstudianteError
import dev.antoine.formularioestudiante.estudiante.models.mappers.toModel
import dev.antoine.formularioestudiante.estudiante.models.service.EstudianteService
import dev.antoine.formularioestudiante.estudiante.models.storage.EstudianteStorage
import dev.antoine.formularioestudiante.estudiante.models.validators.validate
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import dev.antoine.formularioestudiante.locale.round
import dev.antoine.formularioestudiante.locale.toLocalNumber
import org.lighthousegames.logging.logging
import dev.antoine.formularioestudiante.routes.RoutesManager
import java.io.File
import java.time.LocalDate

private val logger = logging()

class FormularioViewModel (
    private val service: EstudianteService,
    private val storage: EstudianteStorage
) {

    // Estado del ViewModel
    val state: SimpleObjectProperty<FormularioState> = SimpleObjectProperty(FormularioState())

    init {
        logger.debug { "Inicializando Formulario ViewModel" }
        loadAllEstudiantes() // Cargamos los datos del alumnado
        loadTypes() // Cargamos los tipos de repetidor
    }

    private fun loadTypes() {
        logger.debug { "Cargando tipos de repetidor" }
        state.value = state.value.copy(typesRepetidor = TipoFiltro.entries.map { it.value })
    }

    private fun loadAllEstudiantes() {
        logger.debug { "Cargando Estudiantes del repositrio" }
        service.findAll().onSuccess {
            logger.debug { "Cargando Estudiantes del repositorio: ${it.size}" }
            state.value = state.value.copy(estudiantes = it)
            updateActualState()
        }
    }

    private fun updateActualState() {
        logger.debug { "Actualizando estado de Aplicacion" }
        val numAprobados = state.value.estudiantes.count { it.isAprobado }.toString()
        val media = state.value.estudiantes.map { it.calificacion }.average()
        val notaMedia = if (media.isNaN()) "0.00" else media.round(2).toLocalNumber()
        // Solo toca el estado una vez para evitar problemas de concurrrencia
       state.value = state.value.copy(
           numAprobados = numAprobados,
           notaMedia = notaMedia,
           estudiante = EstudianteState()
       )
    }


    // Filtra la lista de Estudiantes en el estado en función del tipo y el nombbre completo

    fun esttudiantesFilteredList(tipo: String, nombreCompleto: String): List<Estudiante> {
        logger.debug { "Filtrando lista de Estudiantes: $tipo, $nombreCompleto" }

        return state.value.estudiantes
            .filter { estudiante ->
                when (tipo) {
                    TipoFiltro.TODOS.value -> true
                    TipoFiltro.SI.value -> estudiante.repetidor
                    TipoFiltro.NO.value -> !estudiante.repetidor
                    else -> true
                }
            }.filter { estudiante ->
                estudiante.nombreCompleto.contains(nombreCompleto, true)
            }
    }


    fun saveEstudiantesToJson(file: File): Result<Long, EstudianteError> {
        logger.debug { "Guardando estudiantes en JSON" }
        return storage.storageDataJson(file, state.value.estudiantes)
    }


    fun loadEstudianteFromJson(file: File, withImage: Boolean = false): Result<List<Estudiante>, EstudianteError> {
        logger.debug { "Cargando estudiantes en JSON" }
        //Borramos todas las imagenes e iniociamos el proceso
        return storage.deleteAllImage().andThen {
            storage.loadDataJson(file).onSuccess {
                service.deleteAll() // Borramos todos los datos de la BD
                // Guardamos los nuevos, pero hay que quitale el ID, porque trabajamos con el NEW
                service.saveAll(
                    if (withImage)
                        it
                    else
                        it.map { a -> a.copy(id = Estudiante.NEW_ESTUDIANTE, imagen = TipoImagen.SIN_IMAGEN.value) }
                )
                loadAllEstudiantes() // Actualizamos la lista
            }
        }
    }


    // carga en el estado el Estudiante seleccionado
    fun updateEstudiantesSeleccionados (estudiante: Estudiante) {
        logger.debug { "Actuliazando estado de estudiante: $estudiante" }


        // Datos de la imagen
        var imagen = Image(RoutesManager.getResourcesAsStram("images/NoEcontrado.png"))
        var fileImage = File(RoutesManager.getResource("images/NoEcontrado.png").toURI())

        storage.loadImage(estudiante.imagen).onSuccess {
            imagen = Image(it.absoluteFile.toURI().toString())
            fileImage = it
        }

        state.value = state.value.copy(
            estudiante = EstudianteState(
                numero = estudiante.id.toString(),
                apellidos = estudiante.apellidos,
                nombre = estudiante.nombre,
                email = estudiante.email,
                fechaNacimiento = estudiante.fechaNacimiento,
                calificacion = estudiante.calificacion.round(2).toLocalNumber(),
                repetidor = estudiante.repetidor,
                imagen = imagen,
                fileImage = fileImage
            )
        )

    }


    // Crea un nuevo Estudiante en el estado y repositorio
    fun crearEstudiante(): Result<Estudiante, EstudianteError> {
        logger.debug { "Creando Estudiante" }
        val newEstudianteTemp = state.value.estudiante.copy()
        var newEstudiante = newEstudianteTemp.toModel().copy(id = Estudiante.NEW_ESTUDIANTE)
        return newEstudiante.validate().andThen {
            // Copiamos la imagen si no es nula
            newEstudianteTemp.fileImage?.let { newFileImage ->
                storage.saveImage(newFileImage).onSuccess {
                    newEstudiante = newEstudiante.copy(imagen = it.name)
                }
            }
            // Guardamos el estudiante en el repositorio
            service.save(newEstudiante).andThen {
                state.value = state.value.copy(
                    estudiantes = state.value.estudiantes + it
                )
                updateActualState()
                Ok(it)
            }
        }
    }


    // Edita un estudiante en el estado y repositorio

    fun editarEstudiante(): Result<Estudiante, EstudianteError> {
        logger.debug { "Editando Estudiante" }
        // Actualizamos el estuiante, atención a la imagen

        val updateEstudianteTemp = state.value.estudiante.copy() // Copiamos el estado
        val fileNameTemp = state.value.estudiante.oldFIleImage?.name
            ?: TipoImagen.SIN_IMAGEN // Si no hay imagen, ponemos la sin imagen
        var updateEstudiante = state.value.estudiante.toModel().copy(imagen = fileNameTemp.toString()) // Copiamos el estado a modelo
        return updateEstudiante.validate().andThen {
            // Tenemos dos opciones, que no haya imagen o que si
            updateEstudianteTemp.fileImage?.let { newFileImage ->
                if (updateEstudiante.imagen == TipoImagen.SIN_IMAGEN.value || updateEstudiante.imagen == TipoImagen.EMPTY.value) {
                    // SI no tiene imagen, la guardamos
                    storage.saveImage(newFileImage).onSuccess {
                        updateEstudiante = updateEstudiante.copy(imagen = it.name) // Actualizamos la imagen
                    }
                } else {
                    // Si tiene imagen, la actuliazamos con la nueva, peor hay que borrar la antigua por si cambia
                    storage.updateImage(fileNameTemp.toString(), newFileImage)
                }
            }
            service.save(updateEstudiante).onSuccess {
                // EL estudiante ya está en la lista, saber su posisción
                val index = state.value.estudiantes.indexOfFirst { a -> a.id == it.id }
                state.value = state.value.copy(
                    estudiantes = state.value.estudiantes.toMutableList().apply { this[index] = it}
                )
                updateActualState()
                Ok(it)
            }
        }
    }

    // Elimina un Estudiante en el estado y en el repositorio
    fun eliminarEstudiante(): Result<Unit, EstudianteError> {
        logger.debug { "Eliminando Estudiante" }
        // Hay que eliminar su imagen
        val estudiante = state.value.estudiante.copy()
        // Para evitar que cambie en la selección
        val myId = estudiante.numero.toLong()

        estudiante.fileImage?.let {
            if (it.name != TipoImagen.SIN_IMAGEN.value) {
                storage.deleteImage(it)
            }
        }


        // Borrramos del repositorio
        service.deleteById(myId)
        // Actualizamos la lista
        state.value = state.value.copy(
            estudiantes = state.value.estudiantes.toMutableList().apply { this.removeIf { it.id == myId } }
        )
        updateActualState()
        return Ok(Unit)
    }


    // Actualiza la imagen del estudiante en el estado
    fun updateImageEstudianteOperacion(fileImage: File) {
        logger.debug { "Actualizando Imagen: $fileImage" }
        // Actualizamos la imagen
        state.value = state.value.copy(
            estudiante = state.value.estudiante.copy(
                imagen = Image(fileImage.toURI().toString()),
                fileImage = fileImage,
                oldFIleImage = state.value.estudiante.fileImage // Guardamos la antigua por si hay que cambiar al editar y actuliazar la imagen
            )
        )
    }

    fun exportToZip(fileToZip: File): Result<Unit, EstudianteError> {
        logger.debug { "Exportando a ZIP: $fileToZip" }
        // recogemos los estudiantes del repositorio
        service.findAll().andThen {
            storage.exportToZip(fileToZip, it)
        }.onFailure {
            logger.error { "Error al exportar a ZIP: ${it.message}" }
            return Err(it)
        }
        return Ok(Unit)
    }

    fun loadEstudianteFromZip(fileToUnZip: File): Result<List<Estudiante>, EstudianteError> {
        logger.debug { "Importando de Zip: $fileToUnZip" }
        // recogemos los estudiantes del repositorio
        return storage.loadFromZip(fileToUnZip).onSuccess { lista ->
            service.deleteAll().andThen {
                service.saveAll(lista.map { a -> a.copy(id = Estudiante.NEW_ESTUDIANTE) })
            }.onSuccess {
                loadAllEstudiantes()
            }
        }
    }

    fun changeEstudianteOperacion(newValue: TipoOperacion) {
        logger.debug { "Cambiando tipo de operación: $newValue" }
        if (newValue == TipoOperacion.EDITAR) {
            logger.debug { "Copiando estado de Estudiante Seleeccionado a Operacion" }
            state.value = state.value.copy(
                estudiante = state.value.estudiante.copy(),
                tipoOperacion = newValue
            )

        } else {
            logger.debug { "Limpiando estado de Estudiante Operacion" }
            state.value = state.value.copy(
                estudiante = EstudianteState(),
                tipoOperacion = newValue
            )
        }
    }

    fun updateDataEstudianteOperacion(
        apellidos: String,
        nombre: String,
        email: String,
        fechaNacimiento: LocalDate,
        calificacion: String,
        isRepetidor: Boolean,
        imageEstudiante: Image
    ) {
        logger.debug { "Actualizando estado de Estudiante Operacion" }
        state.value = state.value.copy(
            estudiante = state.value.estudiante.copy(
                apellidos = apellidos,
                nombre = nombre,
                email = email,
                fechaNacimiento = fechaNacimiento,
                calificacion = calificacion,
                repetidor = isRepetidor,
                imagen = imageEstudiante
                // fileImage = fileimage // No se actualiza aqui, se actualiza en el método de la imagen
            )
        )
    }

    // Mi estado
    // Enums
    enum class TipoFiltro(val value: String) {
        TODOS("Todos/as"), SI("Rep: Sí"), NO("Rep: No")
    }

    enum class TipoOperacion(val value: String) {
        NUEVO("Nuevo"), EDITAR("Editar")
    }

    enum class TipoImagen(val value: String) {
        SIN_IMAGEN("NoEncontrado.png"), EMPTY("")
    }

    // Clases que representan al estado
    // Estado de ViewModel y caso de uso de Formularios

    data class FormularioState(
        // Los contenedores de colecciones
        val typesRepetidor: List<String> = emptyList(),
        val estudiantes: List<Estudiante> = emptyList(),

        // Para las estadísticas
        val numAprobados: String = "0",
        val notaMedia: String = "0.0",

        // siempre cambia el tipo de operacion, se actualiza el estudiante
        val estudiante: EstudianteState = EstudianteState(), // Estado del Estudiante seleccionado

        val tipoOperacion: TipoOperacion = TipoOperacion.NUEVO,
    )

    // Estado para formualrios de Estudiante (seleccionado y de operaciones)
    data class EstudianteState(
        val numero: String = "",
        val apellidos: String = "",
        val nombre: String = "",
        val email: String = "",
        val fechaNacimiento: LocalDate = LocalDate.now(),
        val calificacion: String = "",
        val repetidor: Boolean = false,
        val imagen: Image = Image(RoutesManager.getResourcesAsStram("image/NoEnconrado.png")),
        val fileImage: File? = null,
        val oldFIleImage: File? = null, // Para controlar si se ha cambiado la imagen y borrarla
    )

}