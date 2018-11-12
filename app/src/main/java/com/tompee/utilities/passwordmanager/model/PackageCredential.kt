package com.tompee.utilities.passwordmanager.model

import android.graphics.drawable.Drawable

data class PackageCredential(
    val name: String,
    val packageName: String,
    val username: String,
    val password: String,
    val icon: Drawable
) : Comparable<Package> {
    override fun compareTo(other: Package): Int {
        return name.toLowerCase().compareTo(other.name.toLowerCase())
    }
}