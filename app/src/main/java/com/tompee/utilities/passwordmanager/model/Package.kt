package com.tompee.utilities.passwordmanager.model

import android.graphics.drawable.Drawable

data class Package(val name: String, val packageName: String, val icon: Drawable) : Comparable<Package> {

    override fun compareTo(other: Package): Int {
        return name.toLowerCase().compareTo(other.name.toLowerCase())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Package
        if (packageName != other.packageName) return false
        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + packageName.hashCode()
        return result
    }
}