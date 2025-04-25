package dev.antoine.formularioestudiante.storage

import com.github.michaelbull.result.Result
import dev.antoine.formularioestudiante.errors.EstudianteError
import java.io.File

interface EstudianteStorageImages {
    fun saveImage(fileName: File): Result<File, EstudianteError>
    fun loadImage(fileName: String): Result<File, EstudianteError>
    fun deleteimage(fileName: File): Result<Unit, EstudianteError>
    fun deleteAllImage(): Result<Long, EstudianteError>
    fun updateImage(imagenName: String, newFileImage: File): Result<File, EstudianteError>
}