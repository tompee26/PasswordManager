package com.tompee.utilities.passwordmanager.interactor

import com.tompee.utilities.passwordmanager.Constants
import com.tompee.utilities.passwordmanager.base.BaseInteractor
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.datastore.DataStore
import com.tompee.utilities.passwordmanager.model.UserContainer
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class BackupInteractor(
    private val cipher: Cipher,
    private val dataStore: DataStore,
    private val userContainer: UserContainer
) : BaseInteractor {

    fun saveEncryptedIdentifier(key: String): Completable {
        return Single.fromCallable { cipher.encryptWithPassKey(Constants.TEST_IDENTIFIER, key) }
            .flatMapCompletable { dataStore.saveEncryptedIdentifier(userContainer.email, it) }
    }

    fun getEncryptedIdentifier(): Observable<String> {
        return dataStore.getEncryptedIdentifier(userContainer.email)
    }
}