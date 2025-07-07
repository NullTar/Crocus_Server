package com.crucos.web.config

import com.crucos.web.utils.BigIntegerDeserializer
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import java.math.BigInteger


fun Application.contentNegotiation() {
    install(ContentNegotiation) {
        gson {
            // serializeNulls()
            setDateFormat("MM-dd-yyyy HH:mm:ss")
            setDateFormat("MM-dd-yyyy HH:mm:ss E XXX")
            disableHtmlEscaping()
            registerTypeAdapter(BigInteger::class.java, BigIntegerDeserializer())
            setPrettyPrinting()
        }
    }
}