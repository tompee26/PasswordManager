package com.tompee.utilities.passwordmanager.interactor

import com.tompee.utilities.passwordmanager.Constants
import com.tompee.utilities.passwordmanager.base.BaseInteractor
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.database.PackageDao
import com.tompee.utilities.passwordmanager.core.database.SiteDao
import com.tompee.utilities.passwordmanager.core.datastore.DataStore
import com.tompee.utilities.passwordmanager.core.datastore.PackageModel
import com.tompee.utilities.passwordmanager.core.datastore.SiteModel
import com.tompee.utilities.passwordmanager.model.UserContainer
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class BackupInteractor(
    private val cipher: Cipher,
    private val dataStore: DataStore,
    private val packageDao: PackageDao,
    private val siteDao: SiteDao,
    private val userContainer: UserContainer
) : BaseInteractor {

    fun saveEncryptedIdentifier(key: String): Completable {
        return Single.fromCallable { cipher.encryptWithPassKey(Constants.TEST_IDENTIFIER, key) }
            .flatMapCompletable { dataStore.saveEncryptedIdentifier(userContainer.email, it) }
    }

    fun getEncryptedIdentifier(): Observable<String> {
        return dataStore.getEncryptedIdentifier(userContainer.email)
    }

    fun backup(key: String): Completable {
        return dataStore.getEncryptedIdentifier(userContainer.email)
            .firstOrError()
            .flatMapCompletable {
                Completable.fromCallable {
                    cipher.validatePasskey(
                        it,
                        key,
                        Constants.TEST_IDENTIFIER
                    )
                }
            }
            .andThen(dataStore.deletePackages(userContainer.email))
            .andThen(packageDao.getPackages()
                .firstElement()
                .flatMapCompletable { list ->
                    Observable.fromIterable(list)
                        .map {
                            PackageModel(
                                cipher.encryptWithPassKey(it.name, key),
                                cipher.encryptWithPassKey(it.packageName, key),
                                cipher.encryptWithPassKey(cipher.decrypt(it.username, it.packageName), key),
                                cipher.encryptWithPassKey(cipher.decrypt(it.password, it.packageName), key)
                            )
                        }
                        .flatMapCompletable { dataStore.savePackage(userContainer.email, it) }
                })
            .andThen(dataStore.deleteSites(userContainer.email))
            .andThen(siteDao.getSites()
                .firstElement()
                .flatMapCompletable { list ->
                    Observable.fromIterable(list)
                        .map {
                            SiteModel(
                                cipher.encryptWithPassKey(it.siteName, key),
                                cipher.encryptWithPassKey(it.siteUrl, key),
                                cipher.encryptWithPassKey(cipher.decrypt(it.username, it.siteUrl), key),
                                cipher.encryptWithPassKey(cipher.decrypt(it.password, it.siteUrl), key)
                            )
                        }
                        .flatMapCompletable { dataStore.saveSite(userContainer.email, it) }
                })
    }
}