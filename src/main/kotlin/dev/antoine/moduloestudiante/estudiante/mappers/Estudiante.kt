package dev.antoine.moduloestudiante.estudiante.mappers


import dev.antoine.moduloestudiante.estudiante.models.Estudiante
import dev.antoine.moduloestudiante.estudiante.dao.EstudianteEntity
import dev.antoine.moduloestudiante.estudiante.dto.EstudianteDto
import dev.antoine.moduloestudiante.estudiante.viewmodels.ModuloViewModel
import java.time.LocalDate
import java.time.LocalDateTime

fun EstudianteDto.toModel(): Estudiante {
    return Estudiante(
        id,
        nia,
        curso,
        apellidos,
        nombre,
        email,
        LocalDate.parse(fechaNacimiento),
        calificacion,
        repetidor,
        imagen,
        LocalDateTime.parse(createdAt),
        LocalDateTime.parse(updatedAt),
    )
}

@JvmName("dtoToModellist") // Para evitar conflictos cpn el nombre de la función
fun List<EstudianteDto>.toModel(): List<Estudiante> {
    return map { it.toModel() }
}


fun Estudiante.toDto(): EstudianteDto {
    return EstudianteDto(
        id,
        nia,
        curso,
        apellidos,
        nombre,
        email,
        fechaNacimiento.toString(),
        calificacion,
        repetidor,
        imagen,
        createdAt.toString(),
        updatedAt.toString()
    )
}

@JvmName("modelToDtoList")
fun List<Estudiante>.toDto(): List<EstudianteDto> {
    return this.map { it.toDto() }
}

fun EstudianteEntity.toModel(): Estudiante {
    return Estudiante(
        id,
        nia,
        curso,
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
        nia,
        curso,
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

@JvmName("entityToDtoList")
fun List<Estudiante>.toEntity(): List<EstudianteEntity> {
    return this.map { it.toEntity() }
}


fun ModuloViewModel.EstudianteState.toModel(): Estudiante {
    return Estudiante(
        id = if (numero.trim().isBlank()) Estudiante.NEW_ESTUDIANTE else numero.toLong(),
        nia = nia.trim(),
        curso = curso.trim(),
        apellidos = apellidos.trim(),
        nombre = nombre.trim(),
        email = email.trim(),
        fechaNacimiento = fechaNacimiento,
        calificacion = calificacion.trim().replace(",",".").toDouble(),
        repetidor = repetidor,
        imagen = imagen.url ?: "NoEncontrado.png"
    )
}