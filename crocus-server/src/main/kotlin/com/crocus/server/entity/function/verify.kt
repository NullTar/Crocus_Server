package com.crocus.server.entity.function

import com.crocus.server.entity.base.UniversalAccount
import com.crocus.server.utils.regex.RegexEnum
import com.crocus.server.utils.regex.RegexMatch.Companion.isNotMatched
import com.crocus.server.utils.response.Response
import com.crocus.server.utils.sql.Sql.Companion.containKeywords

/**
 * - 1、账户、密码或邮箱     为null或blank    返回 RCode
 * - 2、账户和密码          相同时            返回RCode
 * - 3、账户或密码          长度小于8         返回RCode
 * - 4、账户、密码或邮箱     不匹配正则表达式   返回 RCode
 * - 5、账户或密码          包含数据库关键字   返回 RCode
 * - 否则返回 null
 */
fun UniversalAccount.verify(): Response? {
    return when {
        account.isNullOrBlank() -> Response.ACCOUNT_IS_NULL_OR_BLACK
        password.isNullOrBlank() -> Response.PASSWORD_IS_NULL_OR_BLACK
        email.isNullOrBlank() -> Response.EMAIL_IS_NULL_OR_BLACK

        account == password -> Response.ACCOUNT_EQUALS_PASSWORD

        account!!.length < 8 -> Response.ACCOUNT_LENGTH_UNDER_EIGHT
        password!!.length < 8 -> Response.PASSWORD_LENGTH_UNDER_EIGHT

        isNotMatched(account!!, RegexEnum.ENGLISH) || isNotMatched(password!!, RegexEnum.ENGLISH)
            -> Response.REGEX_NOT_MATCH

        isNotMatched(email!!.lowercase(), RegexEnum.EMAIL) -> Response.EMAIL_REGEX_NOT_MATCH

        verifyKeywords(account!!) || verifyKeywords(password!!)
            -> Response.NOT_ALLOW_INCLUDE_SQL_KEYWORDS

        else -> null
    }
}

/**
 * Account 或 Password
 */
fun verify(value: String): Response? {
    return when {
        value.isBlank() -> Response.ACCOUNT_IS_NULL_OR_BLACK
        verifyKeywords(value) -> Response.NOT_ALLOW_INCLUDE_SQL_KEYWORDS
        value.length < 8 -> Response.ACCOUNT_LENGTH_UNDER_EIGHT
        isNotMatched(value, RegexEnum.ENGLISH) -> Response.REGEX_NOT_MATCH
        else -> null
    }
}

fun verifyKeywords(value: String): Boolean {
    return containKeywords(value)
}

