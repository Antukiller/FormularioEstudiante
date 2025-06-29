package dev.antoine.moduloestudiante.estudiante.models.storage

import dev.antoine.moduloestudiante.estudiante.models.Estudiante
import dev.antoine.moduloestudiante.estudiante.errors.EstudianteError
import java.io.File
import com.github.michaelbull.result.Result

interface EstudianteStorage {
    fun storageDataJson(file: File, data: List<Estudiante>): Result<Long, EstudianteError>
    fun loadDataJson(file: File): Result<List<Estudiante>, EstudianteError>
    fun saveImage(fileName: File): Result<File, EstudianteError>
    fun loadImage(fileName: String): Result<File, EstudianteError>
    fun deleteImage(fileName: File): Result<Unit, EstudianteError>
    fun deleteAllImage(): Result<Long, EstudianteError>
    fun updateImage(imagenName: String, newFileImage: File): Result<File, EstudianteError>
    fun exportToZip(fileToZip: File, data: List<Estudiante>): Result<File, EstudianteError>
    fun loadFromZip(fileToUnZip: File): Result<List<Estudiante>, EstudianteError>
}