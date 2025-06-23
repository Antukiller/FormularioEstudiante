package dev.antoine.moduloestudiante.estudiante.storage

import com.github.michaelbull.result.Result
import dev.antoine.moduloestudiante.estudiante.models.Estudiante
import dev.antoine.moduloestudiante.estudiante.errors.EstudianteError
import java.io.File

interface EstudiantesStorageJson {
    fun storageDataJson(file: File, data: List<Estudiante>): Result<Long, EstudianteError>
    fun loadDataJson(file: File): Result<List<Estudiante>, EstudianteError>
}