package dev.antoine.formularioestudiante.controllers

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.antoine.formularioestudiante.ViewModel.FormularioViewModel
import dev.antoine.formularioestudiante.alumnado.models.Estudiante
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.Cursor.DEFAULT
import javafx.scene.Cursor.WAIT
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lighthousegames.logging.logging
import routes.RoutesManager


private val logger = logging()

class FormularioViewController : KoinComponent {
    // Inyectamos nuestro ViewModel
    val viewModel: FormularioViewModel by inject()

    // Menu
    @FXML
    private lateinit var menuImportar: MenuItem

    @FXML
    private lateinit var menuExportar: MenuItem

    @FXML
    private lateinit var menuZip: MenuItem

    @FXML
    private lateinit var menuUnzip: MenuItem

    @FXML
    private lateinit var menuSalir: MenuItem

    @FXML
    private lateinit var menuAcercaDe: MenuItem

    // Botones
    @FXML
    private lateinit var botonNuevo: Button

    @FXML
    private lateinit var botonEditar: Button

    @FXML
    private lateinit var botonEliminar: Button


    // Combo
    private lateinit var comboTipo: ComboBox<String>

    // Tabla
    @FXML
    private lateinit var tableEstudiante: TableView<Estudiante>

    @FXML
    private lateinit var tableColumNumero: TableColumn<Estudiante, Long>

    @FXML
    private lateinit var tableColumnNombre: TableColumn<Estudiante, String>

    @FXML
    private lateinit var tableColumnCalificacion: TableColumn<Estudiante, Double>

    // Buscador
    @FXML
    private lateinit var textBuscador: TextField

    // Estadísticas
    @FXML
    private lateinit var textNumAprobados: TextField

    @FXML
    private lateinit var textNotaMedia: TextField

    // Formulario del Estudiante
    @FXML
    private lateinit var textEstudianteNumero: TextField

    @FXML
    private lateinit var textEstudianteApellido: TextField

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


    // Metodo para inicializar
    @FXML
    fun initialize() {
        logger.debug { "Inicializando FormularioViewController" }

        initDefaultValues()

        // Iniciamos los default y los bindings
        initBindings()

        // Iniciamos los eventos
        initEventos()
    }

    private fun initDefaultValues() {
        logger.debug { "Inicializando valores por defeto" }

        // comboxBox
        comboTipo.items = FXCollections.observableArrayList(viewModel.state.value.typesRepetidor)
        comboTipo.selectionModel.selectFirst()

        // Tabla
        tableEstudiante.items = FXCollections.observableArrayList(viewModel.state.value.estudiantes)

        // Columnas, con el nombre de la propiedad del objeto hará binding
        tableColumNumero.cellValueFactory = PropertyValueFactory("id")
        tableColumnNombre.cellValueFactory = PropertyValueFactory("nombreCompleto")
        tableColumnCalificacion.cellValueFactory = PropertyValueFactory("calificacionLocale")
    }

    private fun initBindings() {
        textEstudianteNumero.textProperty().bind(viewModel.state.map {it.estudiante.numero})
        textEstudianteApellido.textProperty().bind(viewModel.state.map { it.estudiante.apellidos })
        textEstudianteNombre.textProperty().bind(viewModel.state.map { it.estudiante.nombre })
        textEstudianteEmail.textProperty().bind(viewModel.state.map { it.estudiante.email })
        dateEstudianteFechaNacimiento.valueProperty().bind(viewModel.state.map { it.estudiante.fechaNacimiento })
        textEstudianteCalificacion.textProperty().bind(viewModel.state.map { it.estudiante.calificacion })
        checkEstudianteRepetidor.selectedProperty().bind(viewModel.state.map { it.estudiante.repetidor })
        imageEstudiante.imageProperty().bind(viewModel.state.map { it.estudiante.imagen })

        viewModel.state.addListener { _,_,newValue ->
            logger.debug { "Actulizando datos de la vista" }
            if (tableEstudiante.items != newValue.estudiantes) {
                tableEstudiante.items = FXCollections.observableArrayList(newValue.estudiantes)
            }
        }


    }

    private fun initEventos() {
        logger.debug { "Inicializando eventos" }


        // menus
        menuImportar.setOnAction { onImportarAction() }

        menuExportar.setOnAction { onExportarAction() }

        menuZip.setOnAction { onZipAction() }

        menuUnzip.setOnAction { onUnZipAction() }

        menuSalir.setOnAction { RoutesManager.onAppExit() }

        menuAcercaDe.setOnAction { onAcercaDeAction() }


        // Botones
        botonNuevo.setOnAction { onNuevoAction() }

        botonEditar.setOnAction { onEditatAction() }

        botonEliminar.setOnAction { onEliminarAction() }


        // Combo
        comboTipo.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onComboSelected(newValue) }
        }

        // Tabla
        tableEstudiante.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let { onTablaSelected(newValue) }
        }


        // Buscador
        // Evento del buscador key press
        // Funciona con el intro
        // textBuscador.setOnAction {
        // con cualquer letra al levantarse, ya ha cambiado el valor
        textBuscador.setOnKeyReleased { newValue ->
            newValue?.let { onKeyReleasedAction() }
        }
    }


    private fun onKeyReleasedAction() {
        logger.debug { "onKeyReleasedAction" }
        filterDataTable()
    }

    private fun filterDataTable() {
        logger.debug { "filterDataTable" }
        // filtramos por el tipo seleccionado en la tabla
        tableEstudiante.items =
            FXCollections.observableArrayList(viewModel.esttudiantesFilteredList(comboTipo.value, textBuscador.text.trim()))
    }

    private fun onTablaSelected(newValue: Estudiante) {
        logger.debug { "onTablaSelected: $newValue" }
        viewModel.updateEstudiantesSeleccionados(newValue)
    }

    private fun onComboSelected(newValue: String) {
        logger.debug { "onComboSelected: $newValue" }
        filterDataTable()
    }

    private fun onEliminarAction() {
        logger.debug { "onEliminarAction" }
        if (tableEstudiante.selectionModel.selectedItem == null) {
            return
        }
        Alert(AlertType.CONFIRMATION).apply {
            title = "¿Desea eliminar al estudiante?"
            contentText =
                "¿Desea eliminar al estudiante? Esta acción no se puedes deshacer y se eliminarán todos los datos asociados al estudiante"
        }.showAndWait().ifPresent {
            if (it == ButtonType.OK) {
                viewModel.eliminarEstudiante().onSuccess {
                    logger.debug { "Estudiante eliminado correctamente" }
                    showAlertOperacion(
                        alerta = AlertType.INFORMATION,
                        "Estudiante eliminado",
                        "Se ha eliminado el estudiante correctamente del sistema",
                    )
                    tableEstudiante.selectionModel.clearSelection()
                }.onFailure {
                    logger.debug { "Error al eliminar al estudiante: ${it.message}" }
                    showAlertOperacion(alerta = AlertType.ERROR, "Error al eliminar al estudiante", it.message)
                }
            }
        }
    }


    private fun onEditatAction() {
        logger.debug { "onEditatAction" }
        if (tableEstudiante.selectionModel.selectedItem == null) {
            return
        }
        viewModel.changeEstudianteOperacion(FormularioViewModel.TipoOperacion.EDITAR)
        RoutesManager.initDetalle()
    }

    private fun onNuevoAction() {
        logger.debug { "onNuevoAction" }
        viewModel.changeEstudianteOperacion(FormularioViewModel.TipoOperacion.NUEVO)
        RoutesManager.initDetalle()
    }

    private fun onAcercaDeAction() {
        logger.debug { "onAcercaDeAction" }
        RoutesManager.initDetalle()
    }

    private fun onExportarAction() {
        logger.debug { "onExportarAction" }
        FileChooser().run {
            title = "Exportar Forlumario"
            extensionFilters.add(FileChooser.ExtensionFilter("JSON", "*.json"))
            showSaveDialog(RoutesManager.activateStage)
        }?.let {
            logger.debug { "onExportarAction: $it" }
            RoutesManager.activateStage.scene.cursor = WAIT

            viewModel.saveEstudiantesToJson(it)
                .onSuccess {
                    showAlertOperacion(
                        title = "Datos exportados",
                        mensaje = "Seha exportado tus formularios. Numero de alumnos exportados: ${viewModel.state.value.estudiantes.size}"
                    )
                }.onFailure { error ->
                    showAlertOperacion(alerta = AlertType.ERROR, "Error al exportar", mensaje = error.message)
                }
            RoutesManager.activateStage.scene.cursor = DEFAULT
        }
    }

    private fun onImportarAction() {
        logger.debug { "onImportarAction" }
        // Forma larga, muy Java
        //val fileChooser = FileChooser()
        //fileChooser.title = "Importar expedientes"
        //fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("JSON", "*.json"))
        //val file = fileChooser.showOpenDialog(RoutesManager.activeStage)

        // Forma Kotlin con run y let (scope functions)
        FileChooser().run {
            title = "Importar expedientes"
            extensionFilters.add(FileChooser.ExtensionFilter("JSON", "*.json"))
            showOpenDialog(RoutesManager.activateStage)
        }?.let {
            logger.debug { "onAbrirAction: $it" }
            showAlertOperacion(
                AlertType.INFORMATION,
                "Importando datos",
                "Importando datos Se sustituye la imagen por una imagen por defecto."
            )
            // Cambiar el cursor a espera
            RoutesManager.activateStage.scene.cursor = WAIT
            viewModel.loadEstudianteFromJson(it)
                .onSuccess {
                    showAlertOperacion(
                        title = "Datos importados",
                        mensaje = "Se ha importado tus Expedientes.\nAlumnos importados: ${viewModel.state.value.estudiantes.size}"
                    )
                }.onFailure { error ->
                    showAlertOperacion(alerta = AlertType.ERROR, title = "Error al importar", mensaje = error.message)
                }
            RoutesManager.activateStage.scene.cursor = DEFAULT
        }
    }

    private fun showAlertOperacion(
        alerta: AlertType = AlertType.CONFIRMATION,
        title: String = "",
        mensaje: String = ""
    ) {
        Alert(alerta).apply {
            this.title = title
            this.contentText = mensaje
        }.showAndWait()
    }

    private fun onUnZipAction() {
        logger.debug { "onUnzipAction" }
        FileChooser().run {
            title = "Importar desde Zip"
            extensionFilters.add(FileChooser.ExtensionFilter("ZIP", "*.zip"))
            showOpenDialog(RoutesManager.activateStage)
        }?.let {
            logger.debug { "onAbrirAction: $it" }
            showAlertOperacion(
                AlertType.INFORMATION,
                "Importando datos",
                "Importando datos. Se sustituye la imagen por la imagen encontrada en el zip."
            )
            // Cambiar el cursor a espera
            RoutesManager.activateStage.scene.cursor = WAIT
            viewModel.loadEstudianteFromZip(it)
                .onSuccess {
                    showAlertOperacion(
                        title = "Datos importados",
                        mensaje = "Se ha importado tus Expedientes.\nAlumnos importados: ${viewModel.state.value.estudiantes.size}"
                    )
                }.onFailure { error ->
                    showAlertOperacion(alerta = AlertType.ERROR, title = "Error al importar", mensaje = error.message)
                }
            RoutesManager.activateStage.scene.cursor = DEFAULT
        }

    }

    private fun onZipAction() {
        logger.debug { "onZipAction" }
        FileChooser().run {
            title = "Exportar a Zip"
            extensionFilters.add(FileChooser.ExtensionFilter("ZIP", "*.zip"))
            showSaveDialog(RoutesManager.activateStage)
        }?.let {
            logger.debug { "onAbrirAction: $it" }
            // Cambiar el cursor a espera
            RoutesManager.activateStage.scene.cursor = WAIT
            viewModel.exportToZip(it)
                .onSuccess {
                    showAlertOperacion(
                        title = "Datos exportados",
                        mensaje = "Se ha exportado tus Expedientes completos con imágenes.\nAlumnos exportados: ${viewModel.state.value.estudiantes.size}"
                    )
                }.onFailure { error ->
                    showAlertOperacion(alerta = AlertType.ERROR, title = "Error al exportar", mensaje = error.message)
                }
            RoutesManager.activateStage.scene.cursor = DEFAULT
        }
    }

}



