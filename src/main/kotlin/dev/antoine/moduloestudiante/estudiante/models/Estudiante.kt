package dev.antoine.moduloestudiante.estudiante.models

import dev.antoine.moduloestudiante.locale.toLocalNumber
import java.time.LocalDate
import java.time.LocalDateTime

data class Estudiante(
    val id: Long = NEW_ESTUDIANTE,
    val nia: String,
    val curso: String,
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

    enum class Curso(val Curso: String) {
        PRIMERO_DAW("1ºDAW"),
        SEGUNDO_DAW("2ºDAW")
    }

}