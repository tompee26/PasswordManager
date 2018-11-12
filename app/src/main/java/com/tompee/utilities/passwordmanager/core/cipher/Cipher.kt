package com.tompee.utilities.passwordmanager.core.cipher

import java.security.Key

interface Cipher {
    fun encrypt(data: String, key: Key?): String

    fun decrypt(data: String, key: Key?): String
}