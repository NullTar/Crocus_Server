package com.crocus.server.utils.sql


class Sql {
    companion object {
        fun containKeywords(input: String): Boolean {
            val pattern = "\\b(${SqlKeywords.keywordsSet.joinToString(separator = "|")})\\b"
            val regex = Regex(pattern, RegexOption.IGNORE_CASE)
            return regex.containsMatchIn(input)
        }
    }
}