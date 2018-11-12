package com.tompee.utilities.passwordmanager.interactor

import com.tompee.utilities.passwordmanager.base.BaseInteractor
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.database.PackageDao
import com.tompee.utilities.passwordmanager.core.database.entity.PackageEntity
import com.tompee.utilities.passwordmanager.core.keystore.Keystore
import com.tompee.utilities.passwordmanager.core.packages.PackageManager
import com.tompee.utilities.passwordmanager.model.Package
import io.reactivex.Completable
import io.reactivex.Single

class PackageInteractor(
    private val packageManager: PackageManager,
    private val keystore: Keystore,
    private val cipher: Cipher,
    private val packageDao: PackageDao
) : BaseInteractor {

    fun getPackageList(): Single<List<Package>> = packageManager.getPackages()

    fun savePackageCredential(pack: Package, username: String, password: String): Completable {
        return Single.fromCallable { keystore.createKey(pack.packageName) }
            .map { cipher.encrypt(password, it.public) }
            .map { PackageEntity(pack.packageName, pack.name, username, it) }
            .flatMapCompletable { Completable.fromAction { packageDao.insert(it) } }
    }
}