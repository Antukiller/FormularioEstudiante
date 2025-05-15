package dev.antoine.formularioestudiante.estudiante.controllers

import com.github.michaelbull.result.*
import dev.antoine.formularioestudiante.estudiante.viewmodels.FormularioViewModel
import dev.antoine.formularioestudiante.estudiante.errors.EstudianteError
import dev.antoine.formularioestudiante.estudiante.viewmodels.FormularioViewModel.TipoOperacion.EDITAR
import dev.antoine.formularioestudiante.estudiante.viewmodels.FormularioViewModel.TipoOperacion.NUEVO
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.DatePicker
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lighthousegames.logging.logging
import dev.antoine.formularioestudiante.routes.RoutesManager
import java.time.LocalDate


private val logger = logging()

class DetalleViewController : KoinComponent {
    // Inyectamos  nuestro ViewModel
    val viewModel: FormularioViewModel by inject()

    // Botones
    @FXML
    private lateinit var botonGuardar: Button

    @FXML
    private lateinit var botonLimpar: Button

    @FXML
    private lateinit var botonCancelar: Button


    // Formulario del estudiante
    @FXML
    private lateinit var textEstudianteNumero: TextField

    @FXML
    private lateinit var textEstudianteApellidos: TextField

    @FXML
    private lateinit var textEstudianteNombre: TextField

    @FXML
    private lateinit var textEstudianteEmail: TextField

    @FXML
    private lateinit var dateEstudianteFechaNacimiento: DatePicker

    @FXML
    private lateinit var textEstudianteCalificacion: TextField

    @FXML
    private lateinit var checkEstudianteRepetidor: CheckBox

    @FXML
    private lateinit var imageEstudiante: ImageView



    @FXML
    private fun initialize() {
        logger.debug { "initializing DetalleViewController FXML en Modo: ${viewModel.state.value.tipoOperacion}" }

        textEstudianteNumero.isEditable = false // No se puede editar el número

        // Iniciamos los valores
        initValues()

        // Iniciamos los bindings
        initBindings()

        // Iniciamos los eventos
        initEventos()
    }



    private fun initValues() {
        logger.debug { "initValues" }
        // Inicializamos los valores del formulario
        textEstudianteNumero.text = viewModel.state.value.estudiante.numero
        textEstudianteApellidos.text = viewModel.state.value.estudiante.apellidos
        textEstudianteNombre.text = viewModel.state.value.estudiante.nombre
        textEstudianteEmail.text = viewModel.state.value.estudiante.email
        dateEstudianteFechaNacimiento.value = viewModel.state.value.estudiante.fechaNacimiento
        textEstudianteCalificacion.text = viewModel.state.value.estudiante.calificacion
        checkEstudianteRepetidor.isSelected = viewModel.state.value.estudiante.repetidor
        imageEstudiante.image = viewModel.state.value.estudiante.imagen
    }

    private fun initEventos() {
        // Botones
        botonGuardar.setOnAction {
            onGuardarAction()
        }

        botonLimpar.setOnAction {
            onLimpiarAction()
        }

        botonCancelar.setOnAction {
            onCancelarAction()
        }

        imageEstudiante.setOnMouseClicked {
            onImageAction()
        }
    }

    private fun onImageAction() {
        logger.debug { "onImageAction" }
        // Abrimos un diálogo para seleccionar una imagen
        // Comparalo con los del Json

        FileChooser().run {
            title = "Selecciona una imagen"
            extensionFilters.addAll(FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"))
            showOpenDialog(RoutesManager.activeStage)
        }?.let {
            viewModel.updateImageEstudianteOperacion(it)
        }
    }

    private fun initBindings() {
        // Formulario, actuamos para actualizar solo la imagen poraue los datos ya los hemos cargado!!
        viewModel.state.addListener {_ ,oldValue, newValue ->
            logger.debug { "Actualizando imagen del detalle" }
            // Vamos a poner if, porquie si se actuliza la imagen, no queremos que se actualicen los campos
            if (oldValue.estudiante.imagen != newValue.estudiante.imagen) {
                imageEstudiante.image = newValue.estudiante.imagen
            }
        }
    }


    private fun onGuardarAction() {
        logger.debug { "onGuardarAction" }
       // Dependiendo del modo
        validateForm().andThen {
            // Actualizamos los datos del estudiante operacion del estado!! Como hemos hecho con la imagen!!
            viewModel.updateDataEstudianteOperacion(
                apellidos = textEstudianteApellidos.text,
                nombre = textEstudianteNombre.text,
                email = textEstudianteEmail.text,
                fechaNacimiento = dateEstudianteFechaNacimiento.value,
                calificacion = textEstudianteCalificacion.text,
                isRepetidor = checkEstudianteRepetidor.isSelected,
                imageEstudiante = imageEstudiante.image,
            )

            when (viewModel.state.value.tipoOperacion) {
                NUEVO -> {
                    viewModel.crearEstudiante()
                }

                EDITAR -> {
                    viewModel.editarEstudiante()
                }
            }
        }.onSuccess {
            logger.debug { "Estudiante salvado correctamente" }
            showAlertOperacion(
                Alert.AlertType.INFORMATION,
                "Estudiante salvado",
                "Estudiante salvado correctamente:\n${it.nombreCompleto}"
            )
            cerrarVentana()
        }.onFailure {
            logger.error { "Error al salvado estudiante:${it.message}" }
            showAlertOperacion(
                Alert.AlertType.ERROR,
                "Error al salvar estudiante",
                "Se ha producido un error al salvar el estudiante:\n${it.message}"
            )
        }
    }

    private fun cerrarVentana() {
        // truco coger el stage asociado a un componente
        botonCancelar.scene.window.hide()
    }

    private fun onCancelarAction() {
        logger.debug { "CancelarAction" }
        cerrarVentana()
    }

    private fun onLimpiarAction() {
        logger.debug { "LimpiarAction" }
        limpiarFromulario()
    }

    private fun limpiarFromulario() {
        logger.debug { "LimpiarFromulario" }
        textEstudianteApellidos.clear()
        textEstudianteNombre.clear()
        textEstudianteEmail.clear()
        dateEstudianteFechaNacimiento.value = null
        textEstudianteCalificacion.clear()
        checkEstudianteRepetidor.isSelected = false
        imageEstudiante.image = Image(RoutesManager.getResourceAsStream("images/sin-imagen.png"))
    }


    // Lo puiedo hacer aqui o en mi validador en el viewModel
    private fun validateForm(): Result<Unit, EstudianteError> {
        logger.debug { "validateForm" }

        // Validacion del formualario
        if (textEstudianteApellidos.text.isNullOrEmpty()) {
            return Err(EstudianteError.ValidationProblem("Apellidos no puede estar vacío"))
        }

        if (textEstudianteNombre.text.isNullOrEmpty()) {
            return Err(EstudianteError.ValidationProblem("Nombre no puede estar vacio"))
        }

        // Validamos el email, expresion regular
        if (textEstudianteEmail.text.isNullOrEmpty() || !textEstudianteEmail.text.matches(Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+"))) {
            return Err(EstudianteError.ValidationProblem("Email invalido o no puede estar vacío"))
        }

        if (dateEstudianteFechaNacimiento.value == null || dateEstudianteFechaNacimiento.value.isAfter(LocalDate.now())) {
            return Err(EstudianteError.ValidationProblem("Fecha de nacimiento no puede estar vacía y debe ser anterio a hoy"))
        }

        if (textEstudianteCalificacion.text.isNullOrEmpty() || textEstudianteCalificacion.text.replace(",", ".")
                .toDoubleOrNull() == null || textEstudianteCalificacion.text.replace(",", ".")
                .toDouble() < 0 || textEstudianteCalificacion.text.replace(",", ".").toDouble() > 10
        ) {
            return Err(EstudianteError.ValidationProblem("Calificación no puede estar vacía y debe ser un número entre 0 y 10"))
        }
        return Ok(Unit)

    }

    private fun showAlertOperacion(
        alerta: AlertType = AlertType.CONFIRMATION,
        title: String = "",
        mensaje: String = ""
    ) {
        val alert = Alert(alerta)
        alert.title = title
        alert.contentText = mensaje
        alert.showAndWait()
    }




}