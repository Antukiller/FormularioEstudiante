package dev.antoine.formularioestudiante.storage

import com.github.michaelbull.result.Result
import dev.antoine.formularioestudiante.alumnado.models.Estudiante
import dev.antoine.formularioestudiante.errors.EstudianteError
import java.io.File

interface EstudiantesStorageZip {
    fun exportToZip(fileToZip: File, data: List<Estudiante>): Result<File, EstudianteError>
    fun loadFromZip(fileToUnZip: File): Result<List<Estudiante>, EstudianteError>
}