package com.tompee.utilities.passwordmanager.core.cipher.aes

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.cipher.base.BaseKeystore
import java.nio.ByteBuffer
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class AESCipher : BaseKeystore(), Cipher {

    companion object {
        private const val SYMMETRIC_TRANSFORMATION = "AES/GCM/NoPadding"
    }

    override fun encrypt(data: String, alias: String): String {
        val key = getKey(alias) ?: createKey(alias)

        val cipher = javax.crypto.Cipher.getInstance(SYMMETRIC_TRANSFORMATION)
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key)
        val bytes = cipher.doFinal(data.toByteArray())

        val byteBuffer = ByteBuffer.allocate(4 + cipher.iv.size + bytes.size)
        byteBuffer.putInt(cipher.iv.size)
        byteBuffer.put(cipher.iv)
        byteBuffer.put(bytes)
        return Base64.encodeToString(byteBuffer.array(), Base64.DEFAULT)
    }

    override fun decrypt(data: String, alias: String): String {
        val key = getKey(alias)

        val cipherMessage = Base64.decode(data, Base64.DEFAULT)
        val byteBuffer = ByteBuffer.wrap(cipherMessage)
        val ivLength = byteBuffer.int
        if (ivLength < 12 || ivLength >= 16) { // check input parameter
            throw IllegalArgumentException("invalid iv length")
        }
        val iv = ByteArray(ivLength)
        byteBuffer.get(iv)
        val cipherText = ByteArray(byteBuffer.remaining())
        byteBuffer.get(cipherText)

        val cipher = javax.crypto.Cipher.getInstance(SYMMETRIC_TRANSFORMATION)
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
        val decodedData = cipher.doFinal(cipherText)
        return String(decodedData)
    }

    private fun createKey(alias: String): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, keyStore.provider)
        val spec = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    private fun getKey(alias: String): SecretKey? {
        return (keyStore.getEntry(alias, null) as KeyStore.SecretKeyEntry?)?.secretKey
    }
}