package com.tompee.utilities.passwordmanager.interactor

import com.tompee.utilities.passwordmanager.base.BaseInteractor
import com.tompee.utilities.passwordmanager.core.database.PackageDao
import com.tompee.utilities.passwordmanager.core.packages.PackageManager
import com.tompee.utilities.passwordmanager.model.PackageCredential
import io.reactivex.Observable


class MainInteractor(
    private val packageDao: PackageDao,
    private val packageManager: PackageManager
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
}