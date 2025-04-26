package dev.antoine.formularioestudiante.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.antoine.formularioestudiante.alumnado.models.Estudiante
import dev.antoine.formularioestudiante.dto.EstudianteDto
import dev.antoine.formularioestudiante.errors.EstudianteError
import dev.antoine.formularioestudiante.mappers.toDto
import dev.antoine.formularioestudiante.mappers.toModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.lighthousegames.logging.logging
import java.io.File

private val logger = logging()

class EstudiantesStorageJsonImpl: EstudiantesStorageJson {
    override fun storageDataJson(file: File, data: List<Estudiante>): Result<Long, EstudianteError> {
        logger.debug { "Guardando todos los datos en el fichero: $file" }
        return try {
            val json = Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            }
            val jsonString = json.encodeToString<List<EstudianteDto>>(data.toDto())
            file.writeText(jsonString)
            Ok(data.size.toLong())
        } catch (e: Exception) {
            Err(EstudianteError.SaveJson("Error al escribir el JSON: ${e.message}"))
        }
    }

    override fun loadDataJson(file: File): Result<List<Estudiante>, EstudianteError> {
        logger.debug { "Cargando los datos en el fichero: $file" }
        val json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }
        // Debemos decirle el tipo de dato que queremos pasar
        return try {
            val jsonString = file.readText()
            val data = json.decodeFromString<List<EstudianteDto>>(jsonString)
            Ok(data.toModel())
        } catch (e: Exception) {
            Err(EstudianteError.SaveJson("Error al escribir el JSON: ${e.message}"))
        }
    }

}