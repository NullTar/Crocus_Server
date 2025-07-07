package com.crocus.server.enum

/**
 * @property Yes 是
 * @property No 否
 * @property Agreement 我同意删除我的帐户
 */
enum class Agree(var value: String) {
    Yes("yes"),
    No("not"),
    Agreement("I agree delete my account"),
}
