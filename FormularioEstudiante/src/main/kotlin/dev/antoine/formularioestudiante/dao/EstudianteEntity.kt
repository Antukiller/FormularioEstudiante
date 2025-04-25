package dev.antoine.formularioestudiante.dao

import java.time.LocalDate
import java.time.LocalDateTime

class EstudianteEntity(
    val id: Long,
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