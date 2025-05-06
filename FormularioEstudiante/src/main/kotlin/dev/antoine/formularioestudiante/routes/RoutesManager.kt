package dev.antoine.formularioestudiante.routes

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.image.Image
import javafx.scene.layout.Pane
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.WindowEvent
import org.lighthousegames.logging.logging
import java.io.InputStream
import java.net.URL
import java.util.*
import kotlin.collections.HashMap

private val logger = logging()


/**
 * Clase que gestiona las rutas de la aplicacion
 */

object RoutesManager {
    // Necesitamos siempre saber
    private lateinit var mainStage: Stage // Ventana principal
    private lateinit var _activateStage: Stage // La ventana actual
    val activateStage: Stage
    get() = _activateStage
    lateinit var app: Application // La aplicacion

    // Podemos tener una cache de escenas cargadas
    private var scenesMap: HashMap<String, Pane> = HashMap()

    // Definimos las rutas de las vistas que tengamos
    enum class View(val fxml: String) {
        MAIN("views/estudiante/formulario-view.fxml"),
        DETAILS("views/estudiante/details-view.fxml"),
        ACERCA_DE("views/acerca-de-view.fxml"),
    }

    init {
        logger.debug { "Inicializando RoutesManager" }
        // Podemos configurar el idioma de la aplicacion aqui
        Locale.setDefault(Locale.forLanguageTag("es-ES"))
    }

    /**
     * Recuerda:
     * Si cambiamos de ventana, cambiamos de stage y una scena
     * Si mantenemos la ventana, cambiamos solo la scena
     */

    // Inicializamos la scena principal
    fun initMaainStage(stage: Stage) {
        logger.debug { "Inicializando MainStage" }

        val fxmlLoader = FXMLLoader(javaClass.getResource("View.Main.fxml"))
        val parentRoot = fxmlLoader.load<Pane>()
        val scene = Scene(parentRoot, 680.0, 400.0)
        // scene.stylesheets.add(getResources(Style.DAM.css).toExternalForm())
        stage.title = "Formulario Escolar"
        stage.isResizable = false
        stage.icons.add(Image(getResourcesAsStram("icons/app-icons.png")))
        stage.scene = scene
        mainStage = stage
        _activateStage = stage
        mainStage.show()
    }

    // Repetimos por cada stage configuracion el satge y la scena
    // ....

    // Abrimos one como una nueva ventana

    fun initAcercaDeStage() {
        logger.debug { "Inicializando AcercaDeStage" }
        val fxmlLoader = FXMLLoader(getResource("View.ACERCA_DE.fxml"))
        val parentRoot = fxmlLoader.load<Pane>()
        val scene = Scene(parentRoot, 395.0, 155.0)
        val stage = Stage()
        stage.title = "Formulario Escolar"
        stage.scene = scene
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false

        stage.show()
    }

    // Abrimos one como una nueva ventana
    fun initDetalle() {
        logger.debug { "Inicializando Detalle" }
        val fxmlLoader = FXMLLoader(getResource("View.DETAILS.fxml"))
        val parentRoot = fxmlLoader.load<Pane>()
        val scene = Scene(parentRoot, 350.0, 400.0)
        val stage = Stage()
        stage.title = "Detalle del Estudiante"
        stage.scene = scene
        stage.initOwner(mainStage)
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false

        stage.show()
    }

    // O podemos hacer uno genérico, añade las opciones que necesites
    fun getResource(resource: String): URL {
        return app::class.java.getResource(resource)
            ?: throw RuntimeException("No se ha encontrado el recurso: $resource")
    }

    fun getResourcesAsStram(resource: String): InputStream {
        return app::class.java.getResourceAsStream(resource)
            ?: throw RuntimeException("No se ha encontrado el recurso como stream: $resource")
    }


    fun onAppExit(
        title: String = "Salir de ${mainStage.title}?",
        headerText: String = "¿Estás seguro de que quieres salir de ${mainStage.title}?",
        contentText: String = "Si sales, se cerrará la aplicación y perderás todos los datos no guardados",
        event: WindowEvent? = null
    ) {
        logger.debug { "Cerrando formulario" }
        // Cerramos la aplicacion
        Alert(Alert.AlertType.CONFIRMATION).apply {
            this.title = title
            this.headerText = headerText
            this.contentText = contentText
        }.showAndWait().ifPresent { opcion ->
            if (opcion == ButtonType.OK) {
                // exitProcess(0)
                Platform.exit()
            } else {
                event?.consume()
            }
        }
    }
}