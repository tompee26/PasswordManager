package com.tompee.utilities.passwordmanager.core.biometric

enum class Status {
    OK,
    NON_FATAL_ERROR,
    FATAL_ERROR,
    UNKNOWN
}

data class Result(val type : Status, val message : String)