package com.tompee.utilities.passwordmanager.core.datastore

import androidx.annotation.Keep

@Keep
data class PackageModel(
    val name: String = "",
    val packageName: String = "",
    val username: String = "",
    val password: String = ""
)