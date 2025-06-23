package estudiante.service

import com.github.benmanes.caffeine.cache.Cache
import dev.antoine.moduloestudiante.estudiante.models.Estudiante
import dev.antoine.moduloestudiante.estudiante.repositories.EstudianteRepository
import dev.antoine.moduloestudiante.estudiante.service.EstudiantesServiceImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class EstudiantesServiceImplTest {

    @Mock
    private lateinit var repository: EstudianteRepository

    @Mock
    private lateinit var cache: Cache<Long, Estudiante>

    @InjectMocks
    private lateinit var service: EstudiantesServiceImpl

    private val estudiante = Estudiante(
        id = 1L,
        nia = "12345678",
        curso = "2ºDAW",
        apellidos = "Gómez",
        nombre = "Antu",
        email = "antu@correo.com",
        fechaNacimiento = LocalDate.of(2000, 1, 1),
        calificacion = 7.5,
        repetidor = false,
        imagen = "https://img.url/foto.jpg"
    )

    @Test
    @DisplayName("findAll devuelve lista de estudiantes")
    fun findAllOk() {
        whenever(repository.findAll()).thenReturn(listOf(estudiante))

        val result = service.findAll()

        assertTrue(result.isOk)
        assertEquals(1, result.value.size)
        verify(repository, times(1)).findAll()
    }

    @Test
    @DisplayName("findById devuelve desde caché")
    fun findByIdCacheOk() {
        whenever(cache.getIfPresent(1L)).thenReturn(estudiante)

        val result = service.findById(1L)

        assertTrue(result.isOk)
        assertEquals(estudiante, result.value)
        verify(cache).getIfPresent(1L)
        verify(repository, never()).findById(any())
    }

    @Test
    @DisplayName("findById busca en repositorio si no está en caché")
    fun findByIdRepositoryOk() {
        whenever(cache.getIfPresent(1L)).thenReturn(null)
        whenever(repository.findById(1L)).thenReturn(estudiante)

        val result = service.findById(1L)

        assertTrue(result.isOk)
        assertEquals(estudiante, result.value)
        verify(cache).put(1L, estudiante)
    }

    @Test
    @DisplayName("findById devuelve error si no existe")
    fun findByIdNotFound() {
        whenever(cache.getIfPresent(1L)).thenReturn(null)
        whenever(repository.findById(1L)).thenReturn(null)

        val result = service.findById(1L)

        assertTrue(result.isErr)
    }

    @Test
    @DisplayName("save guarda estudiante y lo pone en caché")
    fun saveOk() {
        whenever(repository.save(estudiante)).thenReturn(estudiante)

        val result = service.save(estudiante)

        assertTrue(result.isOk)
        verify(repository).save(estudiante)
        verify(cache).put(estudiante.id, estudiante)
    }

    @Test
    @DisplayName("saveAll guarda lista y limpia caché")
    fun saveAllOk() {
        val lista = listOf(estudiante)
        whenever(repository.saveAll(lista)).thenReturn(lista)

        val result = service.saveAll(lista)

        assertTrue(result.isOk)
        verify(cache).invalidateAll()
    }

    @Test
    @DisplayName("deleteById elimina y limpia caché")
    fun deleteByIdOk() {
        doNothing().`when`(repository).deleteById(estudiante.id)

        val result = service.deleteById(estudiante.id)

        assertTrue(result.isOk)
        verify(cache).invalidate(estudiante.id)
    }

    @Test
    @DisplayName("deleteAll elimina todos y limpia caché")
    fun deleteAllOk() {
        doNothing().`when`(repository).deleteAll()

        val result = service.deleteAll()

        assertTrue(result.isOk)
        verify(repository).deleteAll()
        verify(cache).invalidateAll()
    }
}
