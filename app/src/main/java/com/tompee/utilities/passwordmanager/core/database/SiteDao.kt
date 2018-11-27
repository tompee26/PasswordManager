package com.tompee.utilities.passwordmanager.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tompee.utilities.passwordmanager.core.database.entity.SiteEntity
import io.reactivex.Observable

@Dao
interface SiteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(siteEntity: SiteEntity)

    @Query("SELECT * FROM site")
    fun getSites(): Observable<List<SiteEntity>>
}