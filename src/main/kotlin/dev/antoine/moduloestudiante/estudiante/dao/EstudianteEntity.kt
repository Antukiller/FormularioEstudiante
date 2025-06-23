package dev.antoine.moduloestudiante.estudiante.dao

import java.time.LocalDate
import java.time.LocalDateTime

data class EstudianteEntity(
    val id: Long,
    val nia: String,
    val curso: String,
    val apellidos: String,
    val nombre: String,
    val email: String,
    val fechaNacimiento: LocalDate,
    val calificacion: Double,
    val repetidor: Boolean,
    val imagen: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)