package dev.antoine.formularioestudiante.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import config.AppConfig
import dev.antoine.formularioestudiante.errors.EstudianteError
import org.lighthousegames.logging.logging
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.time.Instant

private val logger = logging()

class EstudianteStorageImagesImpl(
    private val appConfig: AppConfig
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
        TODO("Not yet implemented")
    }

    override fun deleteimage(fileName: File): Result<Unit, EstudianteError> {
        TODO("Not yet implemented")
    }

    override fun deleteAllImage(): Result<Long, EstudianteError> {
        TODO("Not yet implemented")
    }

    override fun updateImage(imagenName: String, newFileImage: File): Result<File, EstudianteError> {
        TODO("Not yet implemented")
    }
}