package estudiante.validator

import com.github.michaelbull.result.fold
import dev.antoine.moduloestudiante.estudiante.errors.EstudianteError
import dev.antoine.moduloestudiante.estudiante.models.Estudiante
import dev.antoine.moduloestudiante.estudiante.models.validators.validate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.time.LocalDate


class EstudianteValidatorTest {

    private fun estudianteValido() = Estudiante(
        id = 1L,
        nia = "12345678",
        curso = "2ºDAW",
        apellidos = "Gómez",
        nombre = "Antu",
        email = "antu@correo.com",
        fechaNacimiento = LocalDate.of(2000, 1, 1),
        calificacion = 8.0,
        repetidor = false,
        imagen = "imagen.png"
    )

    @Test
    fun `falla si los apellidos están vacíos`() {
        val estudiante = estudianteValido().copy(apellidos = "   ") // solo espacios
        val resultado = estudiante.validate()

        resultado.fold(
            success = { fail("No debería pasar la validación si los apellidos están vacíos") },
            failure = { error ->
                assertTrue(error is EstudianteError.ValidationProblem)
                assertEquals("Los apellidos no pueden estar vacíos", (error as EstudianteError.ValidationProblem).message)
            }
        )
    }


    @Test
    fun `valida correctamente un estudiante válido`() {
        val result = estudianteValido().validate()
        assertTrue(result.isOk)
    }

    @Test
    fun `falla si el nombre está vacío`() {
        val estudiante = estudianteValido().copy(nombre = "")
        val result = estudiante.validate()

        result.fold(
            success = { fail("No debería ser válido si el nombre está vacío") },
            failure = { error ->
                assertTrue(error is EstudianteError.ValidationProblem)
                assertEquals("El nombre no puede estar vacío", (error as EstudianteError.ValidationProblem).message)
            }
        )
    }


    @Test
    fun `falla si el email es incorrecto`() {
        val estudiante = estudianteValido().copy(email = "correo_invalido")
        val result = estudiante.validate()
        assertTrue(result.isErr)
    }

    @Test
    fun `falla si la fecha de nacimiento es futura`() {
        val estudiante = estudianteValido().copy(fechaNacimiento = LocalDate.now().plusDays(1))
        val result = estudiante.validate()
        assertTrue(result.isErr)
    }

    @Test
    fun `falla si la calificación es menor a 0`() {
        val estudiante = estudianteValido().copy(calificacion = -1.0)
        val result = estudiante.validate()
        assertTrue(result.isErr)
    }

    @Test
    fun `falla si la calificación es mayor a 10`() {
        val estudiante = estudianteValido().copy(calificacion = 11.0)
        val result = estudiante.validate()
        assertTrue(result.isErr)
    }

    @Test
    fun `falla si el NIA tiene menos de 8 dígitos`() {
        val estudiante = estudianteValido().copy(nia = "12345")
        val result = estudiante.validate()
        assertTrue(result.isErr)
    }

    @Test
    fun `falla si el curso no es válido`() {
        val estudiante = estudianteValido().copy(curso = "3ºESO")
        val result = estudiante.validate()
        assertTrue(result.isErr)
    }


}