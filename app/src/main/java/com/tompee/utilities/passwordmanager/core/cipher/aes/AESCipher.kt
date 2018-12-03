package com.tompee.utilities.passwordmanager.core.cipher.aes

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.cipher.base.BaseKeystore
import java.lang.Exception
import java.nio.ByteBuffer
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class AESCipher : BaseKeystore(), Cipher {
    companion object {
        private const val SYMMETRIC_TRANSFORMATION = "AES/GCM/NoPadding"
        private const val ITERATIONS = 10835
        private const val KEY_LENGTH = 256
    }

    override fun validatePasskey(encryptedData: String, passKey: String, testData: String) {
        val cipherMessage = Base64.decode(encryptedData, Base64.DEFAULT)
        val byteBuffer = ByteBuffer.wrap(cipherMessage)
        val ivLength = byteBuffer.int
        if (ivLength < 12 || ivLength > 16) { // check input parameter
            throw IllegalArgumentException("invalid iv length")
        }
        val iv = ByteArray(ivLength)
        byteBuffer.get(iv)

        val saltLength = byteBuffer.int
        val salt = ByteArray(saltLength)
        byteBuffer.get(salt)

        val cipherText = ByteArray(byteBuffer.remaining())
        byteBuffer.get(cipherText)

        val cipher = generateCipher(passKey, salt, iv)
        val bytes = cipher.doFinal(testData.toByteArray())
        if (bytes?.contentEquals(cipherText) == false) {
            throw Exception("Invalid key")
        }
    }

    override fun encryptWithPassKey(data: String, passKey: String): String {
        val secureRandom = SecureRandom.getInstanceStrong()
        val salt = ByteArray(16)
        secureRandom.nextBytes(salt)

        val iv = ByteArray(16)
        secureRandom.nextBytes(iv)

        val cipher = generateCipher(passKey, salt, iv)

        val bytes = cipher.doFinal(data.toByteArray())
        val byteBuffer = ByteBuffer.allocate(4 + cipher.iv.size + 4 + salt.size + bytes.size)
        byteBuffer.putInt(cipher.iv.size)
        byteBuffer.put(cipher.iv)
        byteBuffer.putInt(salt.size)
        byteBuffer.put(salt)
        byteBuffer.put(bytes)
        return Base64.encodeToString(byteBuffer.array(), Base64.DEFAULT)
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
        if (ivLength < 12 || ivLength > 16) { // check input parameter
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

    private fun generateCipher(passKey: String, salt: ByteArray, iv: ByteArray): javax.crypto.Cipher {
        val spec = PBEKeySpec(passKey.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val key = factory.generateSecret(spec).encoded
        val keySpec = SecretKeySpec(key, KeyProperties.KEY_ALGORITHM_AES)

        val ivSpec = IvParameterSpec(iv)

        val cipher = javax.crypto.Cipher.getInstance(SYMMETRIC_TRANSFORMATION)
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        return cipher
    }
}