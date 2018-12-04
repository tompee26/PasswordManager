package com.tompee.utilities.passwordmanager.core.cipher

interface Cipher {
    fun encrypt(data: String, alias: String): String

    fun decrypt(data: String, alias: String): String

    fun encryptWithPassKey(data: String, passKey: String): String

    fun validatePasskey(encryptedData: String, passKey: String, testData: String)

    fun decryptWithPassKey(data: String, passKey: String): String
}