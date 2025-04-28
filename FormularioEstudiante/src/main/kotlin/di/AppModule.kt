package di


import com.github.benmanes.caffeine.cache.Cache
import database.provideDatabaseManager
import dev.antoine.formularioestudiante.alumnado.models.Estudiante
import dev.antoine.formularioestudiante.cache.provideEstudianteCache
import dev.antoine.formularioestudiante.dao.EstudianteDao
import dev.antoine.formularioestudiante.dao.provideEstudianteDao
import dev.antoine.formularioestudiante.repositories.EstudianteRepository
import dev.antoine.formularioestudiante.service.EstudianteService
import dev.antoine.formularioestudiante.storage.EstudianteStorageImages
import dev.antoine.formularioestudiante.storage.EstudiantesStorageJson
import dev.antoine.formularioestudiante.storage.EstudiantesStorageZip
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

    singleOf(::EstudianteStorageImpl) {
        bind<EstudiantesStorageJson>()
    }

    singleOf(::EstudianteStorageZipImpl) {
        bind<EstudiantesStorageZip>()
    }

    singleOf(::provideEstudianteImagesImpl) {
        bind<EstudianteStorageImages>()
    }

    singleOf(::EstudianteStorageImpl) {
        bind<EstudiantesStorage>()
    }

    singleOf(::EstudianteServiceImpl) {
        bind<EstudianteService>()
    }

    singleOf(::FormlarioViewModel)


}