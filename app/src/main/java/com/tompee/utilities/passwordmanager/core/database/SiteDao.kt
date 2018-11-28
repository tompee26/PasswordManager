package com.tompee.utilities.passwordmanager.core.database

import androidx.room.*
import com.tompee.utilities.passwordmanager.core.database.entity.SiteEntity
import io.reactivex.Observable

@Dao
interface SiteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(siteEntity: SiteEntity)

    @Query("SELECT * FROM site")
    fun getSites(): Observable<List<SiteEntity>>

    @Delete
    fun remove(siteEntity: SiteEntity)
}