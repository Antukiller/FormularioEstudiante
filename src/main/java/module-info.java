module dev.antoine.moduloestudiante {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Kotlin
    requires kotlin.stdlib;

    // Logger
    requires logging.jvm;
    requires org.slf4j;

    // Kotlin Serialization
    requires kotlinx.serialization.core;
    requires kotlinx.serialization.json;


    // Result
    requires kotlin.result.jvm;

    // SQL
    requires java.sql; // Como no pongas esto te vas a volver loco con los errores!!

    // Koin
    requires koin.core.jvm;

    // Open Vadin
    requires open;

    // JDBI
    requires org.jdbi.v3.sqlobject;
    requires org.jdbi.v3.core;
    requires org.jdbi.v3.kotlin;
    requires org.jdbi.v3.sqlobject.kotlin;
    requires io.leangen.geantyref;
    requires kotlin.reflect;


    // Cache
    requires com.github.benmanes.caffeine;

    // Abrimos y exponemos el paquete a JavaFX
    opens dev.antoine.moduloestudiante to javafx.fxml;
    exports dev.antoine.moduloestudiante;

    // Rutas
    opens dev.antoine.moduloestudiante.routes to javafx.fxml;
    exports dev.antoine.moduloestudiante.routes;



    // Estudiante
    // Controllers
    opens dev.antoine.moduloestudiante.estudiante.controllers to javafx.fxml;

    // dtos
    opens dev.antoine.moduloestudiante.estudiante.dto to javafx.fxml;
    exports dev.antoine.moduloestudiante.estudiante.dto;

    // Modelos a javafx para poder usarlos en las vistas
    opens dev.antoine.moduloestudiante.estudiante.models to javafx.fxml;
    exports dev.antoine.moduloestudiante.estudiante.models;

    // Dao
   opens dev.antoine.moduloestudiante.estudiante.dao to javafx.fxml;
   exports dev.antoine.moduloestudiante.estudiante.dao;

    // Acerca de
    // Controllers
    opens dev.antoine.moduloestudiante.acercade to javafx.fxml;
    exports dev.antoine.moduloestudiante.acercade;

    // Tests kotlin standard library to jupiter.api
    // Para poder usar las librerias de test de kotlin
    // en los test de JUnit
}