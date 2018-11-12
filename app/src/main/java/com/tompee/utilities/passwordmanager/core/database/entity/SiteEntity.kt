package com.tompee.utilities.passwordmanager.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "site",
    primaryKeys = ["name", "url"]
)
data class SiteEntity(
    @ColumnInfo(name = "name")
    val siteName: String,

    @ColumnInfo(name = "url")
    val siteUrl: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "password")
    val password: String
)