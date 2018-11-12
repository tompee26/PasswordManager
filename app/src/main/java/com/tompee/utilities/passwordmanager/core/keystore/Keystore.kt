package com.tompee.utilities.passwordmanager.core.keystore

import java.security.KeyPair

interface Keystore {
    fun createKey(alias: String): KeyPair

    fun getKey(alias: String): KeyPair?
}