package dev.antoine.moduloestudiante.estudiante.repositories

import dev.antoine.moduloestudiante.estudiante.models.Estudiante

interface EstudianteRepository {
    fun findAll(): List<Estudiante>
    fun findById(id: Long): Estudiante?
    fun save(estudiante: Estudiante): Estudiante
    fun deleteById(id: Long)
    fun deleteAll()
    fun saveAll(estudiantes: List<Estudiante>): List<Estudiante>
}