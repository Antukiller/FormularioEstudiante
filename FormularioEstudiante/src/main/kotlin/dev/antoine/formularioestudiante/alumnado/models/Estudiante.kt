package dev.antoine.formularioestudiante.alumnado.models

import locale.toLocalNumber
import java.time.LocalDate
import java.time.LocalDateTime

data class Estudiante(
    val id: Long = NEW_ESTUDIANTE,
    val apellidos: String,
    val nombre: String,
    val email: String,
    val fechaNacimiento: LocalDate,
    val calificacion: Double,
    val repetidor: Boolean,
    val imagen: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)  {
    companion object {
        const val NEW_ESTUDIANTE = -1L
    }

    val nombreCompleto: String
        get() = "$apellidos, $nombre"

    val isNewEstudiante: Boolean
        get() = id == NEW_ESTUDIANTE

    val isAprobado: Boolean
        get() = calificacion >= 5.0

    val calificacionLocale: String
        get() = calificacion.toLocalNumber()
}