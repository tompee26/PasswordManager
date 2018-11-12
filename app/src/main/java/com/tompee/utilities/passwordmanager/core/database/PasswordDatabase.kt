package com.tompee.utilities.passwordmanager.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tompee.utilities.passwordmanager.core.database.entity.PackageEntity
import com.tompee.utilities.passwordmanager.core.database.entity.SiteEntity

@Database(entities = [PackageEntity::class, SiteEntity::class], version = 1, exportSchema = false)
abstract class PasswordDatabase : RoomDatabase() {
    abstract fun packageDao(): PackageDao
    abstract fun siteDao(): SiteDao
}