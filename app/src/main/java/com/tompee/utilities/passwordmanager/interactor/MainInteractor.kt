package com.tompee.utilities.passwordmanager.interactor

import android.content.Context
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseInteractor
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.clipboard.ClipboardManager
import com.tompee.utilities.passwordmanager.core.database.PackageDao
import com.tompee.utilities.passwordmanager.core.database.SiteDao
import com.tompee.utilities.passwordmanager.core.database.entity.PackageEntity
import com.tompee.utilities.passwordmanager.core.database.entity.SiteEntity
import com.tompee.utilities.passwordmanager.core.generator.PasswordGenerator
import com.tompee.utilities.passwordmanager.core.keystore.Keystore
import com.tompee.utilities.passwordmanager.core.packages.PackageManager
import com.tompee.utilities.passwordmanager.feature.common.TextDrawable
import com.tompee.utilities.passwordmanager.model.Package
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
    private val cipher: Cipher,
    private val context: Context,
    private val clipboardManager: ClipboardManager,
    private val passwordGenerator: PasswordGenerator
) : BaseInteractor {

    fun getPackageList(): Observable<List<PackageCredential>> {
        return packageDao.getPackages().concatMap { list ->
            Observable.fromIterable(list)
                .concatMapSingle { entity ->
                    packageManager.getPackageFromName(entity.packageName)
                        .onErrorResumeNext(
                            Single.just(
                                Package(
                                    entity.name,
                                    entity.packageName,
                                    context.getDrawable(R.drawable.ic_app)!!
                                )
                            )
                        )
                        .map {
                            val key = keystore.getKey(it.packageName)!!
                            return@map PackageCredential(
                                it.name,
                                it.packageName,
                                cipher.decrypt(entity.username, key.private),
                                cipher.decrypt(entity.password, key.private),
                                it.icon
                            )
                        }
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

    fun savePackageCredential(pack: Package, username: String, password: String): Completable {
        return Single.fromCallable { keystore.createKey(pack.packageName) }
            .map {
                PackageEntity(
                    pack.packageName,
                    pack.name,
                    cipher.encrypt(username, it.public),
                    cipher.encrypt(password, it.public)
                )
            }
            .flatMapCompletable { Completable.fromAction { packageDao.insert(it) } }
    }

    fun getSiteList(): Observable<List<SiteCredential>> {
        return siteDao.getSites().concatMap { list ->
            Observable.fromIterable(list)
                .map {
                    val key = keystore.getKey(it.siteUrl)!!
                    return@map SiteCredential(
                        it.siteName,
                        it.siteUrl,
                        cipher.decrypt(it.username, key.private),
                        cipher.decrypt(it.password, key.private),
                        TextDrawable(context.resources, it.siteName.toUpperCase().substring(0, 1), false)
                    )
                }
                .toList()
                .toObservable()
        }
    }

    fun copyToClipboard(text: String): Completable = clipboardManager.copyToClipboard(text)

    fun generatePassword(): Single<String> {
        return Single.fromCallable { passwordGenerator.generatePassword(16) }
    }
}