package dev.antoine.formularioestudiante.di


import com.github.benmanes.caffeine.cache.Cache
import dev.antoine.formularioestudiante.config.AppConfig
import dev.antoine.formularioestudiante.database.provideDatabaseManager
import dev.antoine.formularioestudiante.estudiante.models.Estudiante
import dev.antoine.formularioestudiante.estudiante.cache.provideEstudianteCache
import dev.antoine.formularioestudiante.estudiante.dao.EstudianteDao
import dev.antoine.formularioestudiante.estudiante.dao.provideEstudianteDao
import dev.antoine.formularioestudiante.estudiante.repositories.EstudianteRepository
import dev.antoine.formularioestudiante.estudiante.repositories.EstudianteRepositoryImpl
import dev.antoine.formularioestudiante.estudiante.storage.EstudiantesStorageJsonImpl
import dev.antoine.formularioestudiante.estudiante.storage.EstudianteStorageImagesImpl
import dev.antoine.formularioestudiante.estudiante.storage.EstudiantesStorageImpl
import dev.antoine.formularioestudiante.estudiante.models.storage.EstudianteStorageZipImpl
import dev.antoine.formularioestudiante.estudiante.viewmodels.FormularioViewModel
import dev.antoine.formularioestudiante.estudiante.service.EstudiantesServiceImpl
import dev.antoine.formularioestudiante.estudiante.service.EstudianteService
import dev.antoine.formularioestudiante.estudiante.models.storage.EstudianteStorage
import dev.antoine.formularioestudiante.estudiante.storage.EstudianteStorageImages
import dev.antoine.formularioestudiante.estudiante.storage.EstudiantesStorageJson
import dev.antoine.formularioestudiante.estudiante.storage.EstudiantesStorageZip
import org.jdbi.v3.core.Jdbi
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


/**
 * Koin module for the application
 */

val appModule = module {
    singleOf(::AppConfig)

    singleOf(::provideDatabaseManager) {
        bind<Jdbi>()
    }

    singleOf(::provideEstudianteDao) {
        bind<EstudianteDao>()
    }

    singleOf(::provideEstudianteCache) {
        bind<Cache<Long, Estudiante>>()
    }

    singleOf(::EstudianteRepositoryImpl) {
        bind<EstudianteRepository>()
    }

    singleOf(::EstudiantesStorageJsonImpl) {
        bind<EstudiantesStorageJson>()
    }

    singleOf(::EstudianteStorageZipImpl) {
        bind<EstudiantesStorageZip>()
    }

    singleOf(::EstudianteStorageImagesImpl) {
        bind<EstudianteStorageImages>()
    }

    singleOf(::EstudiantesStorageImpl) {
        bind<EstudianteStorage>()
    }

    singleOf(::EstudiantesServiceImpl) {
        bind<EstudianteService>()
    }

    singleOf(::FormularioViewModel)


}