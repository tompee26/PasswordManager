package com.tompee.utilities.passwordmanager.interactor

import com.tompee.utilities.passwordmanager.Constants
import com.tompee.utilities.passwordmanager.base.BaseInteractor
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.database.PackageDao
import com.tompee.utilities.passwordmanager.core.database.SiteDao
import com.tompee.utilities.passwordmanager.core.database.entity.PackageEntity
import com.tompee.utilities.passwordmanager.core.database.entity.SiteEntity
import com.tompee.utilities.passwordmanager.core.datastore.DataStore
import com.tompee.utilities.passwordmanager.core.datastore.PackageModel
import com.tompee.utilities.passwordmanager.core.datastore.SiteModel
import com.tompee.utilities.passwordmanager.model.UserContainer
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class BackupInteractor(
    private val cipher: Cipher,
    private val dataStore: DataStore,
    private val packageDao: PackageDao,
    private val siteDao: SiteDao,
    private val userContainer: UserContainer
) : BaseInteractor {

    fun saveEncryptedIdentifier(key: String): Completable {
        return Single.fromCallable { cipher.encryptWithPassKey(Constants.TEST_IDENTIFIER, key) }
            .flatMapCompletable {
                dataStore.saveEncryptedIdentifier(userContainer.email, it)
                    .subscribeOn(Schedulers.io())
            }
    }

    fun getEncryptedIdentifier(): Observable<String> {
        return dataStore.getEncryptedIdentifier(userContainer.email)
    }

    fun backup(key: String): Completable {
        return dataStore.getEncryptedIdentifier(userContainer.email)
            .firstOrError()
            .flatMapCompletable {
                Completable.fromCallable {
                    cipher.validatePasskey(it, key, Constants.TEST_IDENTIFIER)
                }.subscribeOn(Schedulers.computation())
            }
            .andThen(
                dataStore.deletePackages(userContainer.email)
                    .subscribeOn(Schedulers.io())
            )
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
                        .observeOn(Schedulers.io())
                        .flatMapCompletable { dataStore.savePackage(userContainer.email, it) }
                        .subscribeOn(Schedulers.computation())
                }
                .subscribeOn(Schedulers.io()))
            .andThen(
                dataStore.deleteSites(userContainer.email)
                    .subscribeOn(Schedulers.io())
            )
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
                        .observeOn(Schedulers.io())
                        .flatMapCompletable { dataStore.saveSite(userContainer.email, it) }
                        .subscribeOn(Schedulers.computation())
                }
                .subscribeOn(Schedulers.io()))
    }

    fun clean(key: String): Completable {
        return dataStore.getEncryptedIdentifier(userContainer.email)
            .firstOrError()
            .flatMapCompletable {
                Completable.fromCallable {
                    cipher.validatePasskey(it, key, Constants.TEST_IDENTIFIER)
                }.subscribeOn(Schedulers.computation())
            }
            .andThen(
                dataStore.saveEncryptedIdentifier(userContainer.email, "")
                    .subscribeOn(Schedulers.io())
            )
            .andThen(
                dataStore.deletePackages(userContainer.email)
                    .subscribeOn(Schedulers.io())
            )
            .andThen(
                dataStore.deleteSites(userContainer.email)
                    .subscribeOn(Schedulers.io())
            )
    }

    fun restore(key: String): Completable {
        return dataStore.getEncryptedIdentifier(userContainer.email)
            .firstOrError()
            .flatMapCompletable {
                Completable.fromCallable {
                    cipher.validatePasskey(it, key, Constants.TEST_IDENTIFIER)
                }.subscribeOn(Schedulers.computation())
            }
            .andThen(
                dataStore.getPackages(userContainer.email)
                    .concatMapSingle { packageModel ->
                        Single.fromCallable {
                            val packageName = cipher.decryptWithPassKey(packageModel.packageName, key)
                            return@fromCallable PackageEntity(
                                packageName,
                                cipher.decryptWithPassKey(packageModel.name, key),
                                cipher.encrypt(cipher.decryptWithPassKey(packageModel.username, key), packageName),
                                cipher.encrypt(cipher.decryptWithPassKey(packageModel.password, key), packageName)
                            )
                        }
                            .observeOn(Schedulers.io())
                            .doOnSuccess { packageDao.insert(it) }
                            .subscribeOn(Schedulers.computation())
                    }
                    .toList()
                    .ignoreElement()
                    .subscribeOn(Schedulers.io())
            )
            .andThen(
                dataStore.getSites(userContainer.email)
                    .concatMapSingle { siteModel ->
                        Single.fromCallable {
                            val url = cipher.decryptWithPassKey(siteModel.url, key)
                            return@fromCallable SiteEntity(
                                cipher.decryptWithPassKey(siteModel.name, key),
                                url,
                                cipher.encrypt(cipher.decryptWithPassKey(siteModel.username, key), url),
                                cipher.encrypt(cipher.decryptWithPassKey(siteModel.password, key), url)
                            )
                        }
                            .observeOn(Schedulers.io())
                            .doOnSuccess { siteDao.insert(it) }
                            .subscribeOn(Schedulers.computation())
                    }
                    .toList()
                    .ignoreElement()
                    .subscribeOn(Schedulers.io())
            )
    }
}