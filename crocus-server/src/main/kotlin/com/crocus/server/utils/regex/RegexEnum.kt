package com.crocus.server.utils.regex

import org.intellij.lang.annotations.Language

enum class RegexEnum(val value: String) {
    // 允许字符串中包含英文字母、@ 和 _
    @Language("RegExp")
    ENGLISH(value = "^[a-zA-Z0-9@_-]+$"),

    @Language("RegExp")
    EMAIL(value = "^[a-zA-Z0-9._%+-]+@(icloud\\.com|qq\\.com|gmail\\.com|outlook\\.com|hotmail\\.com|163\\.com|126\\.com|foxmail\\.com)\$"),
}