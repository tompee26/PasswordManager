package com.tompee.utilities.passwordmanager.core.keystore.impl

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.tompee.utilities.passwordmanager.core.keystore.Keystore
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey

class KeystoreImpl : Keystore {

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val ALGO_RSA = "RSA"
    }

    private val keyStore: KeyStore = createAndroidKeyStore()

    override fun createKey(alias: String): KeyPair {
        val generator = KeyPairGenerator.getInstance(ALGO_RSA, ANDROID_KEYSTORE)

        initGeneratorWithKeyGenParameterSpec(generator, alias)
        return generator.generateKeyPair()
    }

    override fun getKey(alias: String): KeyPair? {
        val privateKey = keyStore.getKey(alias, null) as PrivateKey?
        val publicKey = keyStore.getCertificate(alias)?.publicKey

        return if (privateKey != null && publicKey != null) {
            KeyPair(publicKey, privateKey)
        } else {
            null
        }
    }

    private fun initGeneratorWithKeyGenParameterSpec(generator: KeyPairGenerator, alias: String) {
        val builder = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
        generator.initialize(builder.build())
    }

    private fun createAndroidKeyStore(): KeyStore {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        return keyStore
    }
}