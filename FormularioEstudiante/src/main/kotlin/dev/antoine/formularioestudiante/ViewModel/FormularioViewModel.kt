package dev.antoine.formularioestudiante.ViewModel

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.onSuccess
import dev.antoine.formularioestudiante.alumnado.models.Estudiante
import dev.antoine.formularioestudiante.errors.EstudianteError
import dev.antoine.formularioestudiante.service.EstudianteService
import dev.antoine.formularioestudiante.storage.EstudianteStorage
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import org.lighthousegames.logging.logging
import java.io.File

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

    private fun loadAllEstudiantes() {
        logger.debug { "Cargando alumnos del repositrio" }
        service.findAll().onSuccess {
            logger.debug { "Cargando alumnos del repositorio: ${it.size}" }
            state.value = state.value.copy(estudiantes = it)
            updateActualState()
        }
    }

    private fun updateActualState() {
        logger.debug { "Actualizando estado de Aplicacion" }
        val numAprobados = state.value.estudiantes.count { it.isAprobado }.toString()
        val media = state.value.estudiantes.map { it.calificacion }.averrage()
        val notaMedia = if (media.isNaN()) "0.00" else media.round(2).toLocalNumber()
        // Solo toca el estado una vez para evitar problemas de concurrrencia
        state.value = state.value.copy(
            numAprobados = numAprobados,
            media = media,
            estudiante = EstudianteState()
        )
    }


    // Filtra la lista de alumnos en el estado en función del tipo y el nombbre completo

    fun esttudiantesFilteredList(tipo: String, nombreCompleto: String): List<Estudiante> {
        logger.debug { "Filtrando lista de Alumnos: $tipo, $nombreCompleto" }

        return state.value.estudiantes
            .filter { estudiante ->
                when (tipo) {
                    TipoFiltro.TODOS.value -> true
                    TipoFiltro.SI.value -> estudiante.repetidor
                    TipoFlitro.NO.value -> !estudiante.repetidor
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


    fun loadEstudianteFromJson(file: File, withImage: Boolean = false): Result<List<Estudiante>,EstudianteError> {
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


    // carga en el estado el alumno seleccionado
    fun updateEstudiantesSeleccionados (estudiante: Estudiante) {
        logger.debug { "Actuliazando estado de estudiante: $estudiante" }
        // Datos de la imagen

        var imagen = Image(RoutesManager.getResourcesAsStream("images/NoEcontrado.png"))
        var fileImage = File(RoutesManager.getResourcesAsStream("images/NoEcontrado.png").toURI())

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
                calificacion = estudiante.calificacion,
                repetidor = estudiante.repetidor,
                imagen = imagen,
                filtrada = fileImage
            )
        )

    }


    // Crea un nuevo alumno en el estado y repositorio
    fun crearEstudiante(): Result<Estudiante, EstudianteError> {
        logger.debug { "Creando Estudiante" }
        val newEstudianteTemp = state.value.estudiantes.copy()
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


    





}