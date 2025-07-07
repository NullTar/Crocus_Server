package com.crocus.server.utils.encrypt

import java.util.*
import kotlin.random.Random

class UUIDGenerator {
    companion object {

        fun get(): String {
            val uuidByTime = UUID(0, System.currentTimeMillis())
                .toString().replace("0", "").replace("-", "")
            val randomUUIDGenerator = UUID.randomUUID().toString().replace("-", "")
            return uuidByTime + randomUUIDGenerator
        }

        fun generateCode(length: Int = 8): String{
            val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@-_"
            return buildString(length) {
                repeat(length) {
                    append(chars[Random.nextInt(chars.length)])
                }
            }
        }

    }
}
