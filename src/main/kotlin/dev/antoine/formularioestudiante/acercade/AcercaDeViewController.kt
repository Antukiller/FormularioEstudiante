package dev.antoine.formularioestudiante.acercade

import com.vaadin.open.Open
import javafx.fxml.FXML
import org.lighthousegames.logging.logging


private val logger = logging()

class AcercaDeViewController {
    @FXML
    private lateinit var linkGitHub: javafx.scene.control.Hyperlink

    @FXML
    fun initialize() {
        logger.debug { "Inicializamos AcercaDeViewController FXML" }
        linkGitHub.setOnAction {
            val url = "https://github.com/Antukiller"
            logger.debug { "Abriendo navegador en el link: $url" }
            Open.open(url)
        }
    }
}