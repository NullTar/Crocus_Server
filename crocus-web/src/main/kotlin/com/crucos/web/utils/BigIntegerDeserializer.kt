package com.crucos.web.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.math.BigInteger

class BigIntegerDeserializer : JsonDeserializer<BigInteger?> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): BigInteger? {
        return if (json.asString.isNullOrBlank()) null
        else BigInteger(json.asString)
    }
}