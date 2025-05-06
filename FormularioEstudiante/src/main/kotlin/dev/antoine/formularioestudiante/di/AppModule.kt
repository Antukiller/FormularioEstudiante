package dev.antoine.formularioestudiante.di


import com.github.benmanes.caffeine.cache.Cache
import dev.antoine.formularioestudiante.config.AppConfig
import dev.antoine.formularioestudiante.database.provideDatabaseManager
import dev.antoine.formularioestudiante.estudiante.models.Estudiante
import dev.antoine.formularioestudiante.estudiante.models.cache.provideEstudianteCache
import dev.antoine.formularioestudiante.estudiante.models.dao.EstudianteDao
import dev.antoine.formularioestudiante.estudiante.models.dao.provideEstudianteDao
import dev.antoine.formularioestudiante.estudiante.models.repositories.EstudianteRepository
import dev.antoine.formularioestudiante.estudiante.models.repositories.EstudianteRepositoryImpl
import dev.antoine.formularioestudiante.estudiante.models.storage.EstudiantesStorageJsonImpl
import dev.antoine.formularioestudiante.estudiante.models.storage.EstudianteStorageImagesImpl
import dev.antoine.formularioestudiante.estudiante.models.storage.EstudiantesStorageImpl
import dev.antoine.formularioestudiante.estudiante.models.storage.EstudianteStorageZipImpl
import dev.antoine.formularioestudiante.estudiante.models.ViewModel.FormularioViewModel
import dev.antoine.formularioestudiante.estudiante.models.service.EstudiantesServiceImpl
import dev.antoine.formularioestudiante.estudiante.models.service.EstudianteService
import dev.antoine.formularioestudiante.estudiante.models.storage.EstudianteStorage
import dev.antoine.formularioestudiante.estudiante.models.storage.EstudianteStorageImages
import dev.antoine.formularioestudiante.estudiante.models.storage.EstudiantesStorageJson
import dev.antoine.formularioestudiante.estudiante.models.storage.EstudiantesStorageZip
import org.jdbi.v3.core.Jdbi
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


/**
 * Koin module for the aplication
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