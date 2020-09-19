package me.madhead.hoodworking

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.DefaultHeaders
import io.ktor.routing.routing
import me.madhead.hoodworking.routes.webhook

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Compression)

    routing {
        webhook()
    }
}
