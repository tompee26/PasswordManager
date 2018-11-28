package com.tompee.utilities.passwordmanager.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tompee.utilities.passwordmanager.core.database.entity.PackageEntity
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface PackageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(packageEntity: PackageEntity)

    @Query("SELECT * FROM package")
    fun getPackages(): Observable<List<PackageEntity>>

    @Query("SELECT * FROM package WHERE packageName = :name")
    fun findPackage(name: String): Single<PackageEntity>
}