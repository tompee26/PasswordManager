package com.tompee.utilities.passwordmanager.model

import android.graphics.drawable.Drawable

data class Package(val name: String, val packageName: String, val icon: Drawable) : Comparable<Package> {

    override fun compareTo(other: Package): Int {
        return name.toLowerCase().compareTo(other.name.toLowerCase())
    }
}