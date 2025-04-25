package dev.antoine.formularioestudiante.mappers

import dev.antoine.formularioestudiante.alumnado.models.Estudiante
import dev.antoine.formularioestudiante.dao.EstudianteEntity
import dev.antoine.formularioestudiante.dao.EstudiantesDao
import dev.antoine.formularioestudiante.dto.EstudianteDto
import java.time.LocalDate
import java.time.LocalDateTime

fun Estudiante.toModel(): Estudiante {
    return Estudiante(
        id,
        apellidos,
        nombre,
        email,
        LocalDate.parse(fechaNacimiento),
        calificacion,
        repetidor,
        imagen,
        LocalDateTime.parse(createdAt),
        LocalDateTime.parse(updatedAt)
    )
}

@JvmName("dtoToModelList") // Para evitar conflictos con el nombre de la funcion
fun List<Estudiante>.toModel(): List<Estudiante> {
    return map { it.toModel() }
}

fun Estudiante.toDto(): EstudianteDto {
    return EstudianteDto(
        id,
        apellidos,
        nombre,
        email,
        fechaNacimiento.toString(),
        calificacion,
        repetidor,
        imagen,
        createdAt.toString(),
        updatedAt.toString(),
    )
}


@JvmName("modelToDtoList")
fun List<Estudiante>.toDto(): List<EstudianteDto> {
    return map { it.toDto() }
}


fun EstudianteEntity.toModel(): Estudiante {
    return Estudiante(
        id,
        apellidos,
        nombre,
        email,
        fechaNacimiento,
        calificacion,
        repetidor,
        imagen,
        createdAt,
        updatedAt
    )
}

@JvmName("entityToModelList")
fun List<EstudianteEntity>.toModel(): List<Estudiante> {
    return map { it.toModel() }
}

fun Estudiante.toEntity(): EstudianteEntity {
    return EstudianteEntity(
        id,
        apellidos,
        nombre,
        email,
        fechaNacimiento,
        calificacion,
        repetidor,
        imagen,
        createdAt,
        updatedAt
    )
}

@JvmName("modelToEntityList")
fun List<Estudiante>.toEntity(): List<EstudianteEntity> {
    return map { it.toEntity() }
}

fun EstudianteState.toModel(): Estudiante {
    return Estudiante(
        id = if (numero.trim()) Estudiante.NEW_ESTUDIANTE else numero.toLong(),
        apellidos = apellidos.trim(),
        nombre = nombre.trim(),
        email = email.trim(),
        fechaNacimiento = fechaNacimiento,
        calificacion = calificacion.trim().replace(",", ".").toDouble(),
        repetidor = repetidor,
        imagen = imagen.url ?: "NoEncontrado.png"
    )
}