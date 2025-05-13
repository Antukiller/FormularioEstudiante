package dev.antoine.formularioestudiante.estudiante.repositories

import dev.antoine.formularioestudiante.estudiante.models.Estudiante

interface EstudianteRepository {
    fun findAll(): List<Estudiante>
    fun findById(id: Long): Estudiante?
    fun save(estudiante: Estudiante): Estudiante
    fun deleteById(id: Long)
    fun deleteAll()
    fun saveAll(estudiantes: List<Estudiante>): List<Estudiante>
}