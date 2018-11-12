package com.tompee.utilities.passwordmanager.interactor

import com.tompee.utilities.passwordmanager.base.BaseInteractor
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.database.PackageDao
import com.tompee.utilities.passwordmanager.core.database.SiteDao
import com.tompee.utilities.passwordmanager.core.database.entity.SiteEntity
import com.tompee.utilities.passwordmanager.core.keystore.Keystore
import com.tompee.utilities.passwordmanager.core.packages.PackageManager
import com.tompee.utilities.passwordmanager.model.PackageCredential
import com.tompee.utilities.passwordmanager.model.SiteCredential
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single


class MainInteractor(
    private val packageDao: PackageDao,
    private val siteDao: SiteDao,
    private val packageManager: PackageManager,
    private val keystore: Keystore,
    private val cipher: Cipher
) : BaseInteractor {

    fun getPackageList(): Observable<List<PackageCredential>> {
        return packageDao.getPackages().concatMap { list ->
            Observable.fromIterable(list)
                .concatMapSingle { entity ->
                    packageManager.getPackageFromName(entity.packageName)
                        .map { PackageCredential(it.name, it.packageName, entity.username, entity.password, it.icon) }
                }
                .toList()
                .toObservable()
        }
    }

    fun saveCredential(siteName: String, siteUrl: String, username: String, password: String): Completable {
        return Single.fromCallable { keystore.createKey(siteUrl) }
            .map {
                SiteEntity(
                    siteName,
                    siteUrl,
                    cipher.encrypt(username, it.public),
                    cipher.encrypt(password, it.public)
                )
            }
            .flatMapCompletable { Completable.fromAction { siteDao.insert(it) } }
    }

    fun getSiteList(): Observable<List<SiteCredential>> {
        return siteDao.getSites().concatMap { list ->
            Observable.fromIterable(list)
                .map { SiteCredential(it.siteName, it.siteUrl, it.username, it.password) }
                .toList()
                .toObservable()
        }
    }
}