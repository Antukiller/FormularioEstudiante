package dev.antoine.formularioestudiante.estudiante.models.storage

import com.github.michaelbull.result.Result
import dev.antoine.formularioestudiante.config.AppConfig
import dev.antoine.formularioestudiante.estudiante.models.Estudiante
import dev.antoine.formularioestudiante.estudiante.models.errors.EstudianteError
import org.lighthousegames.logging.logging
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

private val logger = logging()

class EstudiantesStorageImpl(
    private val appConfig: AppConfig,
    private val storageJson: EstudiantesStorageJson,
    private val storageZip: EstudiantesStorageZip,
    private val storageImages: EstudianteStorageImages
): EstudianteStorage {
    init {
        // Creamos el directorio de imagenes si no existe
        logger.debug { "Creando directorio de imagenes si no existe" }
        Files.createDirectories(Paths.get(appConfig.imagesDirectory))
    }

    override fun storageDataJson(file: File, data: List<Estudiante>): Result<Long, EstudianteError> {
        logger.debug { "Guardando datos en fichero $file" }
        return storageJson.storageDataJson(file, data)
    }

    override fun loadDataJson(file: File): Result<List<Estudiante>, EstudianteError> {
        logger.debug { "Cargando datos en fichero $file" }
        return storageJson.loadDataJson(file)
    }

    override fun saveImage(fileName: File): Result<File, EstudianteError> {
        logger.debug { "Guardando imagen $fileName" }
        return storageImages.saveImage(fileName)
    }

    override fun loadImage(fileName: String): Result<File, EstudianteError> {
        logger.debug { "Cargando imagen $fileName" }
        return storageImages.loadImage(fileName)
    }

    override fun deleteImage(fileName: File): Result<Unit, EstudianteError> {
        logger.debug { "Borrando imagen $fileName" }
        return storageImages.deleteImage(fileName)
    }

    override fun deleteAllImage(): Result<Long, EstudianteError> {
        logger.debug { "Borrando todas las imgenes" }
        return storageImages.deleteAllImage()
    }

    override fun updateImage(imagenName: String, newFileImage: File): Result<File, EstudianteError> {
        logger.debug { "Actualizando imagen $imagenName" }
        return storageImages.updateImage(imagenName, newFileImage)
    }

    override fun exportToZip(fileToZip: File, data: List<Estudiante>): Result<File, EstudianteError> {
        logger.debug { "Exportando a Zip $fileToZip" }
        return storageZip.exportToZip(fileToZip, data)
    }

    override fun loadFromZip(fileToUnZip: File): Result<List<Estudiante>, EstudianteError> {
        logger.debug { "Importando desde Zip $fileToUnZip" }
        return storageZip.loadFromZip(fileToUnZip)
    }


}