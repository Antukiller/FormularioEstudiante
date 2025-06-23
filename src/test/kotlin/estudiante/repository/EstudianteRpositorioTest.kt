package estudiante.repository

import dev.antoine.moduloestudiante.estudiante.dao.EstudianteDao
import dev.antoine.moduloestudiante.estudiante.mappers.toEntity
import dev.antoine.moduloestudiante.estudiante.models.Estudiante
import dev.antoine.moduloestudiante.estudiante.repositories.EstudianteRepositoryImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class EstudianteRepositoryImplTest {

    @Mock
    private lateinit var dao: EstudianteDao

    @InjectMocks
    private lateinit var repository: EstudianteRepositoryImpl

    private val estudiante = Estudiante(
        id = 1L,
        nia = "12345678",
        curso = "2ºDAW",
        apellidos = "Pérez",
        nombre = "Antu",
        email = "antu@correo.com",
        fechaNacimiento = LocalDate.of(2000, 1, 1),
        calificacion = 7.5,
        repetidor = false,
        imagen = "url/foto.jpg",
        createdAt = LocalDateTime.of(2023, 1, 1, 12, 0),
        updatedAt = LocalDateTime.of(2023, 1, 1, 12, 0)
    )

    private val estudianteEntity = estudiante.toEntity()

    @Test
    fun `findAll devuelve lista de estudiantes`() {
        whenever(dao.selectAll()).thenReturn(listOf(estudiante))

        val result = repository.findAll()

        assertEquals(1, result.size)
        assertEquals(estudiante.nombre, result[0].nombre)
        verify(dao).selectAll()
    }

    @Test
    fun `findById devuelve estudiante cuando existe`() {
        whenever(dao.selectById(1L)).thenReturn(estudianteEntity)

        val result = repository.findById(1L)

        assertEquals(estudiante.id, result!!.id)
        assertEquals(estudiante.nombre, result.nombre)
        verify(dao).selectById(1L)
    }

    @Test
    fun `save llama a create si es nuevo estudiante`() {
        val nuevoEstudiante = estudiante.copy(id = Estudiante.NEW_ESTUDIANTE)
        whenever(dao.insert(any())).thenReturn(100L)

        val result = repository.save(nuevoEstudiante)

        assertEquals(100L, result.id)
        verify(dao).insert(any())
    }

    @Test
    fun `save llama a update si no es nuevo estudiante`() {
        whenever(dao.update(any())).thenReturn(1)

        val result = repository.save(estudiante)

        assertEquals(estudiante.id, result.id)
        verify(dao).update(any())
    }

    @Test
    fun `deleteById llama a dao delete`() {
        whenever(dao.delete(1L)).thenReturn(1)

        repository.deleteById(1L)

        verify(dao).delete(1L)
    }

    @Test
    fun `deleteAll elimina todos los estudiantes`() {
        repository.deleteAll()
        verify(dao).deleteAll()
    }

    @Test
    fun `saveAll guarda lista de estudiantes`() {
        val nuevo = estudiante.copy(id = Estudiante.NEW_ESTUDIANTE)
        whenever(dao.insert(any())).thenReturn(5L)

        val result = repository.saveAll(listOf(nuevo))

        assertEquals(5L, result[0].id)
        verify(dao).insert(any())
    }
}
