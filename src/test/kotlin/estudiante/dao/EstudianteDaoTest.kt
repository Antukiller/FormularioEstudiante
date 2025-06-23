package estudiante.dao

import dev.antoine.moduloestudiante.database.JdbiManager
import dev.antoine.moduloestudiante.estudiante.dao.EstudianteDao
import dev.antoine.moduloestudiante.estudiante.dao.EstudianteEntity
import dev.antoine.moduloestudiante.estudiante.dao.provideEstudianteDao
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.LocalDate
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EstudianteDaoTest {
    private lateinit var dao: EstudianteDao

    private val estudiante = EstudianteEntity(
        id = 1L,
        nia = "20231234",
        curso = "2ºDAW",
        apellidos = "Takahashi",
        nombre = "Haruka",
        email = "haruka@anime.com",
        fechaNacimiento = LocalDate.parse("2000-04-04"),
        calificacion = 7.8,
        repetidor = false,
        imagen = "haruka.png",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    @BeforeAll
    fun setUp() {
        val jdbi = JdbiManager(
            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            databaseInitData = false,
            databaseInitTables = true,
            databaseLogger = false
        ).jdbi
        dao = provideEstudianteDao(jdbi)
    }

    @AfterEach
    fun tearDown() {
        dao.deleteAll()
    }

    @Nested
    @DisplayName("💚 Casos correctos")
    inner class CasosFelices {
        @Test
        fun `insertar estudiante y obtener por id`() {
            val id = dao.insert(estudiante)
            val result = dao.selectById(id)
            assertNotNull(result)
            assertEquals(estudiante.nombre, result?.nombre)
        }

        @Test
        fun `actualizar un estudiante`() {
            val id = dao.insert(estudiante)
            val updated = estudiante.copy(nombre = "Hikaru", id = id)
            val count = dao.update(updated)
            val result = dao.selectById(id)
            assertEquals("Hikaru", result?.nombre)
            assertEquals(1, count)
        }

        @Test
        fun `obtener todos los estudiantes`() {
            dao.insert(estudiante)
            dao.insert(estudiante.copy(nia = "20231235", nombre = "Aiko"))
            val result = dao.selectAll()
            assertEquals(2, result.size)
        }

        @Test
        fun `eliminar un estudiante por id`() {
            val id = dao.insert(estudiante)
            val count = dao.delete(id)
            val result = dao.selectById(id)
            assertEquals(1, count)
            assertNull(result)
        }

        @Test
        fun `eliminar todos los estudiantes`() {
            dao.insert(estudiante)
            dao.insert(estudiante.copy(nia = "20231235"))
            dao.deleteAll()
            val result = dao.selectAll()
            assertTrue(result.isEmpty())
        }
    }

    @Nested
    @DisplayName("💥 Casos incorrectos")
    inner class CasosFallidos {
        @Test
        fun `obtener estudiante inexistente`() {
            val result = dao.selectById(9999L)
            assertNull(result)
        }

        @Test
        fun `eliminar estudiante inexistente`() {
            val result = dao.delete(9999L)
            assertEquals(0, result)
        }

        @Test
        fun `actualizar estudiante inexistente`() {
            val result = dao.update(estudiante.copy(id = 9999L))
            assertEquals(0, result)
        }

        @Test
        fun `obtener todos cuando no hay ninguno`() {
            val result = dao.selectAll()
            assertTrue(result.isEmpty())
        }
    }
}
