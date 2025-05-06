package dev.antoine.formularioestudiante.estudiante.models.dao

import dev.antoine.formularioestudiante.estudiante.models.Estudiante
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.kotlin.RegisterKotlinMapper
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.lighthousegames.logging.logging


@RegisterKotlinMapper(EstudianteEntity::class) // Indicamos que usaremos un mapper de Kotlin, cuidado con los tipos
interface EstudianteDao {

    @SqlUpdate("DELETE FROM Estudiante")
    fun deleteAll()

    @SqlQuery("SELECT * FROM Estudiante ORDER BY apellidos ASC")
    fun selectAll(): List<Estudiante>

    @SqlQuery("SELECT * FROM Estudiante WHERE id = :id")
    fun selectById(@Bind("id") id: Long): EstudianteEntity?

    @SqlUpdate("INSERT INTO Estudiante (apellidos, nombre, email, fechaNacimiento, calificacion, repetidor, imagen, created_at, updated_at) VALUES (:apellidos, :nombre, :email, :fechaNacimiento, :calificacion, :repetidor, :imagen, :createdAt, :updatedAt)")
    @GetGeneratedKeys
    fun insert(@BindBean Estudiante: EstudianteEntity): Long

    @SqlUpdate("UPDATE Estudiante SET apellidos = :apellidos, nombre = :nombre, email = :email, fechaNacimiento = :fechaNacimiento, calificacion = :calificacion, repetidor = :repetidor, imagen = :imagen, updated_at = :updatedAt WHERE id = :id")
    fun update(@BindBean Estudiante: EstudianteEntity): Int

    @SqlUpdate("DELETE FROM Estudiante WHERE id = :id")
    fun delete(@Bind("id") id: Long): Int
}

fun provideEstudianteDao(jdbi: Jdbi): EstudianteDao {
    val logger = logging()
    logger.debug { "Inicializando EstudianteDao" }
    return jdbi.onDemand(EstudianteDao::class.java)
}