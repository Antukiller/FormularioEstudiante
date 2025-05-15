package dev.antoine.formularioestudiante

import dev.antoine.formularioestudiante.di.appModule
import javafx.application.Application
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import dev.antoine.formularioestudiante.routes.RoutesManager
import java.time.LocalDateTime


class FormularioApplication : Application(), KoinComponent {

    init {
        println(LocalDateTime.now())
        // Creamos Koin
        startKoin {
            printLogger() // Logger de Koin
            modules(appModule) // Módulos de Koin
        }
    }

    // Cuando se inicia la aplicación
    override fun start(stage: Stage) {
        // Le pasamos la aplicación a la clase RoutesManager
        RoutesManager.apply {
            app = this@FormularioApplication
        }.run {
            // Iniciamos la aplicación (también se podría usar "also")
            initMainStage(stage)
        }
    }

    // Si quisieras ejecutar código al cerrar la app, puedes descomentar esto:
    /*
    override fun stop() {
        // Código al cerrar la aplicación
    }
    */
}

fun main() {
    // Lanzamos la aplicación
    Application.launch(FormularioApplication::class.java)
}
