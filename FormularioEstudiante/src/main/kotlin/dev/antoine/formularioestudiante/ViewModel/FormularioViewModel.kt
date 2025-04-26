package dev.antoine.formularioestudiante.ViewModel

import dev.antoine.formularioestudiante.service.EstudianteService
import dev.antoine.formularioestudiante.storage.EstudianteStorage
import javafx.beans.property.SimpleObjectProperty
import org.lighthousegames.logging.logging

private val logger = logging()

class FormularioViewModel (
    private val service: EstudianteService,
    private val storage: EstudianteStorage
) {

    // Estado del ViewModel
    val state: SimpleObjectProperty<FormularioState> = SimpleObjectProperty(FormularioState())

    init {
        logger.debug { "Inicializando Formulario ViewModel" }
        loadAllEstudiantes() // 
    }


}