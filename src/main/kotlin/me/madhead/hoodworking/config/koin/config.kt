package me.madhead.hoodworking.config.koin

import io.ktor.config.ApplicationConfig
import io.ktor.util.KtorExperimentalAPI
import org.koin.dsl.module

@KtorExperimentalAPI
fun configModule(config: ApplicationConfig) = module {
    single {
        config
    }
}
