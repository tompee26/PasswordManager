package com.tompee.utilities.passwordmanager.core.datastore

import io.reactivex.Completable
import io.reactivex.Observable

interface DataStore {
    fun saveEncryptedIdentifier(email: String, key: String): Completable

    fun getEncryptedIdentifier(email: String): Observable<String>

    fun savePackage(email: String, pack: PackageModel): Completable

    fun saveSite(email: String, site: SiteModel): Completable

    fun deletePackages(email: String): Completable

    fun deleteSites(email: String): Completable

    fun getPackages(email: String): Observable<PackageModel>

    fun getSites(email: String): Observable<SiteModel>
}