package dev.antoine.formularioestudiante.estudiante.storage

import com.github.michaelbull.result.Result
import dev.antoine.formularioestudiante.estudiante.models.Estudiante
import dev.antoine.formularioestudiante.estudiante.errors.EstudianteError
import java.io.File

interface EstudiantesStorageJson {
    fun storageDataJson(file: File, data: List<Estudiante>): Result<Long, EstudianteError>
    fun loadDataJson(file: File): Result<List<Estudiante>, EstudianteError>
}