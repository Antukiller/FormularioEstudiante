package dev.antoine.formularioestudiante.estudiante.repositories

import dev.antoine.formularioestudiante.estudiante.models.Estudiante
import dev.antoine.formularioestudiante.estudiante.dao.EstudianteDao
import dev.antoine.formularioestudiante.estudiante.mappers.toEntity
import dev.antoine.formularioestudiante.estudiante.mappers.toModel
import org.lighthousegames.logging.logging
import java.time.LocalDateTime

private val logger = logging()

class EstudianteRepositoryImpl(
    private val dao: EstudianteDao
): EstudianteRepository {


    override fun findAll(): List<Estudiante> {
        logger.debug { "Find all" }
        return dao.selectAll()
    }

    override fun findById(id: Long): Estudiante? {
        logger.debug { "FindById: $id" }
        return dao.selectById(id)?.toModel()
    }

    override fun save(estudiante: Estudiante): Estudiante {
       logger.debug { "Save: $estudiante" }
        return if (estudiante.isNewEstudiante) {
            create(estudiante)
        } else {
            update(estudiante)
        }
    }

    private fun create(estudiante: Estudiante): Estudiante {
        logger.debug { "Create $estudiante" }
        val timeStamp = LocalDateTime.now()
        val toSave = estudiante.copy(createdAt = timeStamp, updatedAt = timeStamp)
        val id = dao.insert(toSave.toEntity())
        return toSave.copy(id = id)
    }


    private fun update(estudiante: Estudiante): Estudiante {
        logger.debug { "Update $estudiante" }
        val timeStamp = LocalDateTime.now()
        val toUpdate = estudiante.copy(updatedAt = timeStamp)
        val res = dao.update(toUpdate.toEntity())
        logger.debug { "Nuestra consulta de actulizacion ha vuelto: $res" }
        logger.debug { "Estudiante eliminado con id: $toUpdate" }
        return toUpdate
    }

    override fun deleteById(id: Long) {
        logger.debug { "DeleteById: $id" }
        val res = dao.delete(id)
        logger.debug { "Nuestra consulta de borrado ha devuelto: $res" }
        logger.debug { "Estudiante eliminado con id: $id" }
    }

    override fun deleteAll() {
        logger.debug { "DeleteAll" }
        return dao.deleteAll()
    }

    override fun saveAll(estudiantes: List<Estudiante>): List<Estudiante> {
        logger.debug { "SaveAll: $estudiantes" }
        return estudiantes.map { save(it) }
    }

}