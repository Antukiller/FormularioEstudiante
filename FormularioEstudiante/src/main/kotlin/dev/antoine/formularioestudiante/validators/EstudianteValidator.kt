package dev.antoine.formularioestudiante.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.antoine.formularioestudiante.alumnado.models.Estudiante
import dev.antoine.formularioestudiante.errors.EstudianteError
import java.time.LocalDate

fun Estudiante.validate(): Result<Estudiante, EstudianteError> {
    if (this.nombre.isEmpty() || this.nombre.isBlank()) {
        return Err(EstudianteError.ValidationProblem("El nombre no puede estar vacío"))
    }
    if (this.apellidos.isEmpty() || this.apellidos.isBlank()) {
        return Err(EstudianteError.ValidationProblem("Los apellidos no pueden estar vacíos"))
    }

    if (this.email.isEmpty() || !Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$").matches(this.email)) {
        return Err(EstudianteError.ValidationProblem("El email no puede estar vacío"))
    }
    if (this.fechaNacimiento.isAfter(LocalDate.now())) {
        return Err(EstudianteError.ValidationProblem("La fecha de nacimiento no puede ser posterior a hoy"))
    }
    if (this.calificacion < 0 || this.calificacion > 10) {
        return Err(EstudianteError.ValidationProblem("La calificación debe estar entre 0 y 10"))
    }
    return Ok(this)
}