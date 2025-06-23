package dev.antoine.moduloestudiante.estudiante.storage

import com.github.michaelbull.result.Result
import dev.antoine.moduloestudiante.estudiante.models.Estudiante
import dev.antoine.moduloestudiante.estudiante.errors.EstudianteError
import java.io.File

interface EstudiantesStorageZip {
    fun exportToZip(fileToZip: File, data: List<Estudiante>): Result<File, EstudianteError>
    fun loadFromZip(fileToUnZip: File): Result<List<Estudiante>, EstudianteError>
}