package me.madhead.hoodworking.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.application
import io.ktor.routing.post

fun Route.webhook() {
    post(application.environment.config.property("telegram.token").getString()) {
        call.respond(HttpStatusCode.OK)
    }
}
