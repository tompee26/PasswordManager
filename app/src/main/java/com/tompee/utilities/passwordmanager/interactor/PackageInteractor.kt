package com.tompee.utilities.passwordmanager.interactor

import com.tompee.utilities.passwordmanager.base.BaseInteractor
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.database.PackageDao
import com.tompee.utilities.passwordmanager.core.database.entity.PackageEntity
import com.tompee.utilities.passwordmanager.core.generator.PasswordGenerator
import com.tompee.utilities.passwordmanager.core.keystore.Keystore
import com.tompee.utilities.passwordmanager.core.packages.PackageManager
import com.tompee.utilities.passwordmanager.model.Package
import com.tompee.utilities.passwordmanager.model.PackageCredential
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class PackageInteractor(
    private val packageManager: PackageManager,
    private val keystore: Keystore,
    private val cipher: Cipher,
    private val packageDao: PackageDao,
    private val passwordGenerator: PasswordGenerator
) : BaseInteractor {

    fun getPackageList(): Observable<List<Package>> = packageManager.getPackages()
        .flatMapObservable { originalPackageList ->
            getRegisteredPackageList()
                .map { originalPackageList.minus(it) }
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

    fun generatePassword(): Single<String> {
        return Single.fromCallable { passwordGenerator.generatePassword(16) }
    }

    private fun getRegisteredPackageList(): Observable<List<Package>> {
        return packageDao.getPackages().concatMap { list ->
            Observable.fromIterable(list)
                .concatMapSingle { entity ->
                    packageManager.getPackageFromName(entity.packageName)
                        .map { Package(it.name, it.packageName, it.icon) }
                }
                .toList()
                .toObservable()
        }
    }
}