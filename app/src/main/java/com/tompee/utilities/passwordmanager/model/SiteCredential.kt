package com.tompee.utilities.passwordmanager.model

import android.graphics.drawable.Drawable

data class SiteCredential(
    val name: String,
    val url: String,
    val username: String,
    val password: String,
    val icon: Drawable
)