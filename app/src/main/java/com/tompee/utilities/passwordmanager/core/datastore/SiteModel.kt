package com.tompee.utilities.passwordmanager.core.datastore

import androidx.annotation.Keep

@Keep
data class SiteModel(
    val name: String = "",
    val url: String = "",
    val username: String = "",
    val password: String = ""
)