package dev.antoine.moduloestudiante.di


import com.github.benmanes.caffeine.cache.Cache
import dev.antoine.moduloestudiante.config.AppConfig
import dev.antoine.moduloestudiante.database.provideDatabaseManager
import dev.antoine.moduloestudiante.estudiante.models.Estudiante
import dev.antoine.moduloestudiante.estudiante.cache.provideEstudianteCache
import dev.antoine.moduloestudiante.estudiante.dao.EstudianteDao
import dev.antoine.moduloestudiante.estudiante.dao.provideEstudianteDao
import dev.antoine.moduloestudiante.estudiante.repositories.EstudianteRepository
import dev.antoine.moduloestudiante.estudiante.repositories.EstudianteRepositoryImpl
import dev.antoine.moduloestudiante.estudiante.storage.EstudiantesStorageJsonImpl
import dev.antoine.moduloestudiante.estudiante.storage.EstudianteStorageImagesImpl
import dev.antoine.moduloestudiante.estudiante.storage.EstudiantesStorageImpl
import dev.antoine.moduloestudiante.estudiante.models.storage.EstudianteStorageZipImpl
import dev.antoine.moduloestudiante.estudiante.service.EstudiantesServiceImpl
import dev.antoine.moduloestudiante.estudiante.service.EstudianteService
import dev.antoine.moduloestudiante.estudiante.models.storage.EstudianteStorage
import dev.antoine.moduloestudiante.estudiante.storage.EstudianteStorageImages
import dev.antoine.moduloestudiante.estudiante.storage.EstudiantesStorageJson
import dev.antoine.moduloestudiante.estudiante.storage.EstudiantesStorageZip
import dev.antoine.moduloestudiante.estudiante.viewmodels.ModuloViewModel
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

    singleOf(::ModuloViewModel)


}