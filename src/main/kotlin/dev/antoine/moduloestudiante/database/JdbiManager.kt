package dev.antoine.moduloestudiante.database

import dev.antoine.moduloestudiante.config.AppConfig
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.statement.SqlLogger
import org.jdbi.v3.core.statement.StatementContext
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.lighthousegames.logging.logging
import java.io.File
import java.time.temporal.ChronoUnit

/**
 * Clase que gestiona la conexión con la base de datos
 * y la creación de tablas y carga de datos
 */

class JdbiManager(
    private val databaseUrl: String,
    private val databaseInitData: Boolean,
    private val databaseInitTables: Boolean,
    private val databaseLogger: Boolean
) {
    private val logger = logging()


    // La creo de forma lazy para que se cree cuando se necesite
    val jdbi by lazy { Jdbi.create(databaseUrl) }


    init {
        logger.debug { "Inicializando JdbiManager" }


        // instalamos los plugins
        jdbi.installPlugin(KotlinPlugin()) // Necesario para trabajar con Kotlin
        jdbi.installPlugin(SqlObjectPlugin()) // Necesario para trabajar con SQLObject, DAO

        // Configuramos el logger de SQL si es necesario
        if (databaseLogger) {
            val sqlLogger: SqlLogger = object : SqlLogger {
                override fun logAfterExecution(context: StatementContext) {
                    logger.debug {
                        "Query executed: " + "sql ${context.renderedSql}, parameters ${context.binding}, timeTaken ${
                            context.getElapsedTime(
                                ChronoUnit.MILLIS
                            )
                        } ms"
                    }
                }
            }
            jdbi.setSqlLogger(sqlLogger)
        }


        if (databaseInitTables) {
            logger.debug { "Creando tablas" }
            // Leemos el fichero de resources
            executeSqlScriptFromResources("tables.sql")
        }
        if (databaseInitData) {
            logger.debug { "Cargando datos" }
            executeSqlScriptFromResources("data.sql")
        }
    }


    /**
     * Ejecuta un script SQL
     * @param scriptFilePath Ruta del fichero
     */
    fun executeSqlScript(scriptFilePath: String) {
        logger.debug { "Ejecutando script SQL: $scriptFilePath" }
        val script = File(scriptFilePath).readText()
        jdbi.useHandle<Exception> { handle ->
            handle.createScript(script).execute()
        }
    }

    /**
     * Ejectua un script SQL desde un recurso
     * @param resourcePath Ruta del recursos
     */
    fun executeSqlScriptFromResources(resourcePath: String) {
        logger.debug { "Ejecutando script SQL desde recursos: $resourcePath" }
        val inputStream = ClassLoader.getSystemResourceAsStream(resourcePath)?.bufferedReader()!!
        val script = inputStream.readText()
        jdbi.useHandle<Exception> { handle ->
            handle.createScript(script).execute()
        }
    }
}

fun provideDatabaseManager(config: AppConfig): Jdbi {
    val logger = logging()
    logger.debug { "Proporcionando instancia de JdbiManager" }
    return JdbiManager(
        config.databaseUrl,
        config.databaseInitData,
        config.databaseInitTables,
        config.databaseLogger
    ).jdbi
}