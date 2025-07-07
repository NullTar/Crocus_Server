package com.crocus.server.utils.type

import com.fasterxml.jackson.core.type.TypeReference

inline fun <reified T> typeRef(): TypeReference<T> {
    return object : TypeReference<T>() {}
}