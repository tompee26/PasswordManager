package com.tompee.utilities.passwordmanager.core.datastore

import io.reactivex.Completable
import io.reactivex.Observable

interface DataStore {
    fun saveEncryptedIdentifier(email: String, key: String): Completable

    fun getEncryptedIdentifier(email: String): Observable<String>
}