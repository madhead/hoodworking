package me.madhead.hoodworking

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.DefaultHeaders
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import me.madhead.hoodworking.config.koin.configModule
import me.madhead.hoodworking.config.koin.dbModule
import me.madhead.hoodworking.config.koin.jsonModule
import me.madhead.hoodworking.config.koin.pipelineModule
import me.madhead.hoodworking.config.koin.telegramModule
import me.madhead.hoodworking.routes.webhook
import org.koin.ktor.ext.Koin

@KtorExperimentalAPI
fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Compression)
    install(Koin) {
        modules(
                configModule(environment.config),
                telegramModule,
                jsonModule,
                dbModule,
                pipelineModule,
        )
    }

    routing {
        webhook()
    }
}
