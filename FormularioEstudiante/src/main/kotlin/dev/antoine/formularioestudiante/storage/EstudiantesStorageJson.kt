package dev.antoine.formularioestudiante.storage

import com.github.michaelbull.result.Result
import dev.antoine.formularioestudiante.alumnado.models.Estudiante
import dev.antoine.formularioestudiante.errors.EstudianteError
import java.io.File

interface EstudiantesStorageJson {
    fun storageDataJson(file: File, data: List<Estudiante>): Result<Long, EstudianteError>
    fun loadDataJson(file: File): Result<List<Estudiante>, EstudianteError>
}