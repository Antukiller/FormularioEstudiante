package dev.antoine.moduloestudiante.estudiante.service

import com.github.benmanes.caffeine.cache.Cache
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.antoine.moduloestudiante.estudiante.models.Estudiante
import dev.antoine.moduloestudiante.estudiante.errors.EstudianteError
import dev.antoine.moduloestudiante.estudiante.repositories.EstudianteRepository
import org.lighthousegames.logging.logging

class EstudiantesServiceImpl(
    private val repository: EstudianteRepository,
    private val cache: Cache<Long, Estudiante>
): EstudianteService {
    private val logger = logging()
    override fun findAll(): Result<List<Estudiante>, EstudianteError> {
        logger.debug { "Finding all Estudiantes" }
        return Ok(repository.findAll())
    }

    override fun deleteAll(): Result<Unit, EstudianteError> {
        logger.debug { "Deleting all Estudiantes" }
        repository.deleteAll().also {
            cache.invalidateAll()
            return Ok(it)
        }
    }

    override fun saveAll(estudiante: List<Estudiante>): Result<List<Estudiante>, EstudianteError> {
        logger.debug { "Saving all Estudiantes" }
        repository.saveAll(estudiante).also {
            cache.invalidateAll()
            return Ok(it)
        }
    }

    override fun save(estudiante: Estudiante): Result<Estudiante, EstudianteError> {
        logger.debug { "Saving Estudiante" }
        repository.save(estudiante).also { nuevoEstudiante ->
            cache.put(nuevoEstudiante.id, estudiante)
            logger.debug { "Estudiante salvado/actualizado: $nuevoEstudiante" }
            return Ok(nuevoEstudiante)
        }
    }

    override fun deleteById(id: Long): Result<Unit, EstudianteError> {
        logger.debug { "Delete by id $id" }
        repository.deleteById(id).also {
            cache.invalidate(id)
            return Ok(it)
        }
    }

    override fun findById(id: Long): Result<Estudiante, EstudianteError> {
        logger.debug { "Find by id $id" }
        return cache.getIfPresent(id)?.let {
            Ok(it)
        } ?: repository.findById(id)?.also {
            cache.put(id, it)
        }?.let {
            Ok(it)
        }?: Err(EstudianteError.NotFound("Estudiante con ID no encontrado"))
    }


}