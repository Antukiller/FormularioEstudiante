package dev.antoine.moduloestudiante.estudiante.models.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.antoine.moduloestudiante.estudiante.models.Estudiante
import dev.antoine.moduloestudiante.estudiante.errors.EstudianteError
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
    if (this.nia.isEmpty() || !Regex("^\\d{8}\$").matches(this.nia)) {
        return Err(EstudianteError.ValidationProblem("El NIA debe tener 8 dígitos numéricos"))
    }

    if (this.curso.isEmpty() || Estudiante.Curso.values().none { it.Curso == this.curso }) {
        return Err(EstudianteError.ValidationProblem("El curso no puede estar vacío y debe ser 1ºDAW o 2ºDAW"))
    }





    return Ok(this)
}