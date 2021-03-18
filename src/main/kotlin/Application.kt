package no.sonhal

import io.ktor.application.*
import io.ktor.routing.*
import com.github.mustachejava.DefaultMustacheFactory
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.mustache.*
import io.ktor.request.*
import io.ktor.response.*
import no.nav.security.token.support.ktor.tokenValidationSupport
import org.slf4j.event.Level



fun main(args: Array<String>) {
    io.ktor.server.cio.EngineMain.main(args)
}


@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    val config = this.environment.config

    install(CallLogging) {
        level = Level.INFO
    }

    install(Mustache) {
        mustacheFactory = DefaultMustacheFactory("templates/mustache")
    }


    install(Authentication) {
        tokenValidationSupport(config = config)
    }

    routing {
        trace {
            application.log.trace(it.buildText())
        }

        get("/") {
            call.respond(MustacheContent("index.hbs",
                mapOf(
                    "request_headers" to headersFromCall(call.request.headers),
                    "request_cookies" to cookiesFromCall(call.request.cookies),
                    "request_query_params" to queryParamsFromCall(call.request.queryParameters),
                    "response_headers" to headersFromCall(call.response.headers.allValues()),
                    "status" to statusFromResponse(call.response.status())
                )))
        }

        static("/css") {
            resources("static/css")
        }

        authenticate {
            get("/safe") {
                call.respond(MustacheContent("authorized.hbs",
                    mapOf(
                        "request_headers" to headersFromCall(call.request.headers),
                        "request_cookies" to cookiesFromCall(call.request.cookies),
                        "request_query_params" to queryParamsFromCall(call.request.queryParameters),
                        "response_headers" to headersFromCall(call.response.headers.allValues()),
                        "status" to statusFromResponse(call.response.status())
                    )
                ))
            }
        }

        get("/openhello") {
            call.respondText("<b>Hello in the open</b>", ContentType.Text.Html)
        }
    }
}

data class Header(val name: String, val value: String)

data class Cookie(val name: String, val value: String)

data class Paramenter(val key: String, val value: String)

data class Status(val id: Int?, val description: String?)

fun statusFromResponse(status: HttpStatusCode?) =
    Status(status?.value, status?.description )

fun headersFromCall(headers: Headers) =
    headers.entries().map { entry -> entry.value.map { Header(entry.key, it) } }.flatten()

fun cookiesFromCall(cookies: RequestCookies) =
    cookies.rawCookies.map { Cookie(it.key, it.value) }

fun queryParamsFromCall(params: Parameters) =
    params.entries().map { entry -> entry.value.map { Paramenter(entry.key, it) } }.flatten()