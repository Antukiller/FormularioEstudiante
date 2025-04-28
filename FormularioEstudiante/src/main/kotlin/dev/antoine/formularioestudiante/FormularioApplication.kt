package dev.antoine.formularioestudiante

import javafx.application.Application
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import routes.RoutesManager
import java.time.LocalDateTime

class FormularioApplication : Application(), KoinComponent {

    init {
        println(LocalDateTime.now().toString())
        // creamos koin
        startKoin {
            printLogger() // Logger de koin
            modules(appModule) // Moudulos de koin
        }
    }

    // Cuando se inicia la aplicación
    override fun start(stage: Stage) {
        // Le pasamos la aplicación a la clase RoutesManager
        RoutesManager.apply {
            app = this@FormularioApplication
        }.run {
            // Iniciamos la aplicación, podiamos hacerlo con also
            initMaainStage(stage)
        }
    }
}

// Cuando se para la aplicacion
/*
override fun stop() {
    // No hacemos nada
 */

fun main() {
    // No hacemos nada aqui porque se haga un hilo de ejecución
    Application.launch(FormularioApplication::class.java)
}