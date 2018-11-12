package com.tompee.utilities.passwordmanager.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tompee.utilities.passwordmanager.core.database.entity.PackageEntity

@Database(entities = [PackageEntity::class], version = 1, exportSchema = false)
abstract class PasswordDatabase : RoomDatabase() {
    abstract fun packageDao(): PackageDao
}