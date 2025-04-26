package dev.antoine.formularioestudiante.service

import com.github.michaelbull.result.Result
import dev.antoine.formularioestudiante.alumnado.models.Estudiante
import dev.antoine.formularioestudiante.errors.EstudianteError

interface EstudianteService {
    fun findAll(): Result<List<Estudiante>, EstudianteError>
    fun deleteAll(): Result<Unit, EstudianteError>
    fun saveAll(estudiante: List<Estudiante>): Result<List<Estudiante>, EstudianteError>
    fun save(estudiante: Estudiante): Result<Estudiante, EstudianteError>
    fun deleteById(id: Long): Result<Unit, EstudianteError>
    fun findById(id: Long): Result<Estudiante, EstudianteError>
}