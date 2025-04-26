package dev.antoine.formularioestudiante.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import config.AppConfig
import dev.antoine.formularioestudiante.alumnado.models.Estudiante
import dev.antoine.formularioestudiante.errors.EstudianteError
import org.lighthousegames.logging.logging
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import kotlin.io.path.name

private val logger = logging()

class EstudianteStorageZipImpl(
    private val appConfig: AppConfig,
    private val storageJson: EstudiantesStorageJson,
): EstudiantesStorageZip {

    private val tempDirName = "Estudiantes"

    override fun exportToZip(fileToZip: File, data: List<Estudiante>): Result<File, EstudianteError> {
        logger.debug { "Exportando a ZIP $fileToZip" }
        // Creamos el directorio temporal
        val tempDir = Files.createTempDirectory(tempDirName)
        // copiamos todas las imagenes al directorio temporal
        return try {

            data.forEach {
                val file = File(appConfig.imagesDirectory + it.imagen)
                if (file.exists()) {
                    Files.copy(
                        file.toPath(),
                        Paths.get(tempDir.toString(), file.name),
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
            }
            // exportamos un json con el listado al directorio temporal
            storageJson.storageDataJson(File("$tempDir/data.json"), data)
            // Listamos por consola el contenido del directorio temporal
            Files.walk(tempDir).forEach { logger.debug { it } }
            // Eliminamos el directorio temporal al terminar
            // comprimimos
            val archivos = Files.walk(tempDir)
                .filter { Files.isRegularFile(it) }
                .toList()
            ZipOutputStream(Files.newOutputStream(fileToZip.toPath())).use { zip ->
                archivos.forEach { archivo ->
                    val entradaZip = ZipEntry(tempDir.relativize(archivo).toString())
                    zip.putNextEntry(entradaZip)
                    Files.copy(archivo, zip)
                    zip.closeEntry()
                }
            }
            tempDir.toFile().deleteRecursively()
            Ok(fileToZip)
        } catch (e: Exception) {
            logger.error { "Error al exportar a ZIP: ${e.message}" }
            Err(EstudianteError.ExportZip("Error al exportar a ZIP: ${e.message}"))
        }
    }

    override fun loadFromZip(fileToUnZip: File): Result<List<Estudiante>, EstudianteError> {
        logger.debug { "Importando desde ZIP $fileToUnZip" }
        // Creamos el directorio temporal
        val tempDir = Files.createTempDirectory(tempDirName)
        return try {
            // Descomprimimos a un directorio temporal
            ZipFile(fileToUnZip).use { zip ->
                zip.entries().asSequence().forEach { entrada ->
                    zip.getInputStream(entrada).use { input ->
                        Files.copy(
                            input,
                            Paths.get(tempDir.toString(), entrada.name),
                            StandardCopyOption.REPLACE_EXISTING
                        )
                    }
                }
            }
            // Listamos por consola el contenido del directorio temporal
            Files.walk(tempDir).forEach {
                // copiamos todas las imagenes, es decir, todo lo que no es .json al directorio de imagenes
                if (!it.toString().endsWith(".json") && Files.isRegularFile(it)) {
                    Files.copy(
                        it,
                        Paths.get(appConfig.imagesDirectory, it.name),
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
            }
            // tomamos el fichero data.json y lo parseamos a una lista de alumnos
            val data = storageJson.loadDataJson(File("$tempDir/data.json"))
            tempDir.toFile().deleteRecursively()
            return data
        } catch (e: Exception) {
            logger.error { "Error al importar desde ZIP: ${e.message}" }
            Err(EstudianteError.ImportZip("Error al importar desde ZIP: ${e.message}"))
        }
    }
}

