package dev.antoine.formularioestudiante.estudiante.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.antoine.formularioestudiante.config.AppConfig
import dev.antoine.formularioestudiante.estudiante.errors.EstudianteError
import org.lighthousegames.logging.logging
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.Instant

private val logger = logging()

class EstudianteStorageImagesImpl(
    private val appConfig: AppConfig,
): EstudianteStorageImages {

    private fun getImagenName(newFileImage: File): String {
        val name = newFileImage.name
        val extension = name.substring(name.lastIndexOf('.') + 1)
        return "${Instant.now().toEpochMilli()}.$extension"
    }


    override fun saveImage(fileName: File): Result<File, EstudianteError> {
        logger.debug { "Saving image $fileName" }
        return try {
            val newFileImage = File(appConfig.imagesDirectory + getImagenName(fileName))
            Files.copy(fileName.toPath(), newFileImage.toPath(), StandardCopyOption.REPLACE_EXISTING)
            Ok(newFileImage)
        } catch (e: Exception) {
            Err(EstudianteError.SaveImage("Error al guardar la imagen: ${e.message}"))
        }
    }

    override fun loadImage(fileName: String): Result<File, EstudianteError> {
        logger.debug { "Loading image $fileName" }
        val file = File(appConfig.imagesDirectory + fileName)
        return if (file.exists()) {
            Ok(file)
        } else {
            Err(EstudianteError.SaveImage("La imagen no existe :(${file.name})"))
        }
    }

    override fun deleteImage(fileName: File): Result<Unit, EstudianteError> {
        logger.debug { "Deleting image $fileName" }
        Files.deleteIfExists(fileName.toPath())
        return Ok(Unit)
    }

    override fun deleteAllImage(): Result<Long, EstudianteError> {
        logger.debug { "Deleting all images" }
        return try {
            Ok(
                Files.walk(Paths.get(appConfig.imagesDirectory))
                    .filter { Files.isRegularFile(it) }
                    .map { Files.deleteIfExists(it) }
                    .count()
            )
        } catch (e: Exception) {
            Err(EstudianteError.DeleteImage("Error al borrar todas las imagénes : ${e.message}"))
        }
    }

    override fun updateImage(imagenName: String, newFileImage: File): Result<File, EstudianteError> {
        logger.debug { "Updating image $imagenName" }
        return try {
            val newUpdateImage = File(appConfig.imagesDirectory + imagenName)
            Files.copy(newFileImage.toPath(), newUpdateImage.toPath(), StandardCopyOption.REPLACE_EXISTING)
            Ok(newFileImage)
        } catch (e: Exception) {
            Err(EstudianteError.SaveImage("Error al guardar la imagen: ${e.message}"))
        }
    }
}