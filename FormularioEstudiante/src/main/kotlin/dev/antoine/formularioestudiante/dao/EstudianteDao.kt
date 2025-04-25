package dev.antoine.formularioestudiante.dao

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.kotlin.RegisterKotlinMapper
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.lighthousegames.logging.logging


@RegisterKotlinMapper(EstudianteEntity::class) // Indicamos que usaremos un mapper de Kotlin, cuidado con los tipos
interface EstudiantesDao {

    @SqlUpdate("DELETE FROM Estudiantes")
    fun deleteAll()

    @SqlQuery("SELECT * FROM Estudiantes ORDER BY apellidos ASC")
    fun selectAll(): List<EstudianteEntity>

    @SqlQuery("SELECT * FROM Estudiantes WHERE id = :id")
    fun selectById(@Bind("id") id: Long): EstudianteEntity?

    @SqlUpdate("INSERT INTO Estudiantes (apellidos, nombre, email, fechaNacimiento, calificacion, repetidor, imagen, created_at, updated_at) VALUES (:apellidos, :nombre, :email, :fechaNacimiento, :calificacion, :repetidor, :imagen, :createdAt, :updatedAt)")
    @GetGeneratedKeys
    fun insert(@BindBean Estudiante: EstudianteEntity): Long

    @SqlUpdate("UPDATE Estudiantes SET apellidos = :apellidos, nombre = :nombre, email = :email, fechaNacimiento = :fechaNacimiento, calificacion = :calificacion, repetidor = :repetidor, imagen = :imagen, updated_at = :updatedAt WHERE id = :id")
    fun update(@BindBean Estudiante: EstudianteEntity): Int

    @SqlUpdate("DELETE FROM Estudiantes WHERE id = :id")
    fun delete(@Bind("id") id: Long): Int
}

fun provideEstudiantesDao(jdbi: Jdbi): EstudiantesDao {
    val logger = logging()
    logger.debug { "Inicializando EstudiantesDao" }
    return jdbi.onDemand(EstudiantesDao::class.java)
}