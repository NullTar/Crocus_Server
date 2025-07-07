package com.crocus.server.utils.regex

class RegexMatch {

    companion object {
        /**
         * 校验数据是否匹配正则表达式
         * @param str 需要校验的字符串 String
         * @param rule 需要校验的正则表达式 RegexEnum
         */
        fun isNotMatched(str: String, rule: RegexEnum): Boolean {
            val regex = Regex(rule.value)
            return !str.matches(regex)
        }
    }
}
