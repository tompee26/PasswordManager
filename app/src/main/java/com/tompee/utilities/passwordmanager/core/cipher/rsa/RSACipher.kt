package com.tompee.utilities.passwordmanager.core.cipher.rsa

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.cipher.base.BaseKeystore
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey

class RSACipher : BaseKeystore(), Cipher {

    companion object {
        private const val ASYMMETRIC_TRANSFORMATION = "RSA/ECB/PKCS1Padding"
    }

    override fun encrypt(data: String, alias: String): String {
        val key = getKey(alias) ?: createKey(alias)

        val cipher = javax.crypto.Cipher.getInstance(ASYMMETRIC_TRANSFORMATION)
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key.public)
        val bytes = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    override fun decrypt(data: String, alias: String): String {
        val key = getKey(alias)
        val cipher = javax.crypto.Cipher.getInstance(ASYMMETRIC_TRANSFORMATION)
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key?.private)
        val encryptedData = Base64.decode(data, Base64.DEFAULT)
        val decodedData = cipher.doFinal(encryptedData)
        return String(decodedData)
    }

    private fun createKey(alias: String): KeyPair {
        val generator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, keyStore.provider)
        val builder = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
        generator.initialize(builder.build())
        return generator.generateKeyPair()
    }

    private fun getKey(alias: String): KeyPair? {
        val privateKey = keyStore.getKey(alias, null) as PrivateKey?
        val publicKey = keyStore.getCertificate(alias)?.publicKey

        return if (privateKey != null && publicKey != null) {
            KeyPair(publicKey, privateKey)
        } else {
            null
        }
    }
}
