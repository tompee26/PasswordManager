package com.tompee.utilities.passwordmanager.core.cipher.impl

import android.util.Base64
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import java.security.Key

class CipherImpl : Cipher {

    companion object {
        private const val TRANSFORMATION_ASYMMETRIC = "RSA/ECB/PKCS1Padding"
    }

    override fun encrypt(data: String, key: Key?): String {
        val cipher = javax.crypto.Cipher.getInstance(TRANSFORMATION_ASYMMETRIC)
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key)
        val bytes = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    override fun decrypt(data: String, key: Key?): String {
        val cipher = javax.crypto.Cipher.getInstance(TRANSFORMATION_ASYMMETRIC)
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key)
        val encryptedData = Base64.decode(data, Base64.DEFAULT)
        val decodedData = cipher.doFinal(encryptedData)
        return String(decodedData)
    }
}