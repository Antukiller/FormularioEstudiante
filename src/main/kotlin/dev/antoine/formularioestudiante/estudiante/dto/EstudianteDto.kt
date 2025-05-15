package dev.antoine.formularioestudiante.estudiante.dto

import kotlinx.serialization.Serializable

@Serializable
data class EstudianteDto(
    val id: Long,
    val apellidos: String,
    val nombre: String,
    val email: String,
    val fechaNacimiento: String,
    val calificacion: Double,
    val repetidor: Boolean,
    val imagen: String,
    val createdAt: String,
    val updatedAt: String
)