package com.crocus.server.utils.exception

import com.crocus.server.utils.response.Response
import java.lang.Exception

class E(response: Response, cause: Throwable? = null) : Exception(response.message, cause) {
    val code = response.code
}