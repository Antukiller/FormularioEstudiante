package dev.antoine.moduloestudiante.estudiante.cache

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import dev.antoine.moduloestudiante.config.AppConfig
import dev.antoine.moduloestudiante.estudiante.models.Estudiante
import org.lighthousegames.logging.logging
import java.util.concurrent.TimeUnit

fun provideEstudianteCache(config: AppConfig): Cache<Long, Estudiante> {
    val logger = logging()
    logger.debug { "Inicializando Cache con capacidad ${config.cacheCapacity} y duracion ${config.cacheExpiration} s" }
    return Caffeine.newBuilder()
        .maximumSize(config.cacheCapacity)
        .expireAfterWrite(
            config.cacheExpiration,
            TimeUnit.SECONDS
        )
        .build()
}