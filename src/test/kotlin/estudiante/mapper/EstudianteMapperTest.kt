package estudiante.mapper

import dev.antoine.moduloestudiante.estudiante.mappers.toDto
import dev.antoine.moduloestudiante.estudiante.mappers.toEntity
import dev.antoine.moduloestudiante.estudiante.mappers.toModel
import dev.antoine.moduloestudiante.estudiante.models.Estudiante
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime



class EstudianteMapperTest {


    @Test
    fun `modelo a DTO y de vuelta mantiene valores importantes`() {
        val estudiante = Estudiante(
            id = 1L,
            nia = "87654321",
            curso = "2ºDAW",
            apellidos = "López Martínez",
            nombre = "Antu",
            email = "antu@ejemplo.com",
            fechaNacimiento = LocalDate.of(2004, 6, 15),
            calificacion = 6.3,
            repetidor = false,
            imagen = "https://img.url/foto.jpg",
            createdAt = LocalDateTime.of(2024, 12, 1, 10, 0),
            updatedAt = LocalDateTime.of(2024, 12, 1, 10, 0)
        )

        val dto = estudiante.toDto()
        val modeloConvertido = dto.toModel()

        assertEquals(estudiante.nombre, modeloConvertido.nombre)
        assertEquals(estudiante.apellidos, modeloConvertido.apellidos)
        assertEquals(estudiante.fechaNacimiento, modeloConvertido.fechaNacimiento)
    }

    @Test
    fun `modelo a entidad y de vuelta mantiene valores importantes`() {
        val estudiante = Estudiante(
            id = 1L,
            nia = "87654321",
            curso = "2ºDAW",
            apellidos = "López Martínez",
            nombre = "Antu",
            email = "antu@ejemplo.com",
            fechaNacimiento = LocalDate.of(2004, 6, 15),
            calificacion = 6.3,
            repetidor = false,
            imagen = "https://img.url/foto.jpg",
            createdAt = LocalDateTime.of(2024, 12, 1, 10, 0),
            updatedAt = LocalDateTime.of(2024, 12, 1, 10, 0)
        )

        val entidad = estudiante.toEntity()
        val modelo = entidad.toModel()

        assertEquals(estudiante.nia, modelo.nia)
        assertEquals(estudiante.email, modelo.email)
        assertEquals(estudiante.imagen, modelo.imagen)
    }

    @Test
    fun `conversión de listas de modelos a DTO y viceversa funciona correctamente`() {
        val estudiante1 = Estudiante(
            id = 1L,
            nia = "87654321",
            curso = "2ºDAW",
            apellidos = "López Martínez",
            nombre = "Antu",
            email = "antu@ejemplo.com",
            fechaNacimiento = LocalDate.of(2004, 6, 15),
            calificacion = 6.3,
            repetidor = false,
            imagen = "https://img.url/foto.jpg",
            createdAt = LocalDateTime.of(2024, 12, 1, 10, 0),
            updatedAt = LocalDateTime.of(2024, 12, 1, 10, 0)
        )
        val estudiante2 = estudiante1.copy(id = 2L, nombre = "Lucía")

        val listaDto = listOf(estudiante1, estudiante2).toDto()
        val modelos = listaDto.toModel()

        assertEquals(2, modelos.size)
        assertEquals("Lucía", modelos[1].nombre)
    }
}
