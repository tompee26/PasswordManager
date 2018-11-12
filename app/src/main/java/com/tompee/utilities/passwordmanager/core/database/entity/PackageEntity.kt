package com.tompee.utilities.passwordmanager.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "package",
    primaryKeys = ["packageName", "name"]
)
data class PackageEntity(
    @ColumnInfo(name = "packageName")
    val packageName: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "password")
    val password: String
)