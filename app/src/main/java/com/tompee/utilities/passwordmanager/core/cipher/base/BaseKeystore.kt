package com.tompee.utilities.passwordmanager.core.cipher.base

import java.security.KeyStore

abstract class BaseKeystore {
    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    }

    protected val keyStore: KeyStore = createAndroidKeyStore()

    private fun createAndroidKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        return keyStore
    }
}