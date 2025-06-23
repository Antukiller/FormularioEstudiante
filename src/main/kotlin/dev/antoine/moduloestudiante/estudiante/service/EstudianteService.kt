package dev.antoine.moduloestudiante.estudiante.service

import com.github.michaelbull.result.Result
import dev.antoine.moduloestudiante.estudiante.models.Estudiante
import dev.antoine.moduloestudiante.estudiante.errors.EstudianteError

interface EstudianteService {
    fun findAll(): Result<List<Estudiante>, EstudianteError>
    fun deleteAll(): Result<Unit, EstudianteError>
    fun saveAll(estudiante: List<Estudiante>): Result<List<Estudiante>, EstudianteError>
    fun save(estudiante: Estudiante): Result<Estudiante, EstudianteError>
    fun deleteById(id: Long): Result<Unit, EstudianteError>
    fun findById(id: Long): Result<Estudiante, EstudianteError>
}