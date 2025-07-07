package com.crucos.web.utils

import io.ktor.network.tls.certificates.*
import java.io.File
import java.security.KeyStore

/**
 * @author junsilck
 * @date 2024/5/27 21:38
 * @description TODO
 */
class SSL {
    val keyStoreFile = File("crocus-web/build/keystore.jks")
    lateinit var keyStore: KeyStore

    init {
        this.init()
    }

    /**
     * 初始化
     */
    private fun init() {
        keyStore = buildKeyStore {
            certificate("sslAlias"){
                password = "JunSilck@732613"
                domains = listOf("0.0.0.0","127.0.0.1","localhost")
            }
        }
        keyStore.saveToFile(keyStoreFile, "732613")
    }
}
