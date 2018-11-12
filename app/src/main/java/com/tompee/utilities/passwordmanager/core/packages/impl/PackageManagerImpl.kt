package com.tompee.utilities.passwordmanager.core.packages.impl

import android.content.Context
import com.tompee.utilities.passwordmanager.core.packages.PackageManager
import com.tompee.utilities.passwordmanager.model.Package
import io.reactivex.Observable
import io.reactivex.Single

class PackageManagerImpl(private val context: Context) : PackageManager {

    override fun getPackages(): Single<List<com.tompee.utilities.passwordmanager.model.Package>> {
        return Single.fromCallable { context.packageManager.getInstalledApplications(android.content.pm.PackageManager.GET_META_DATA) }
            .flatMap { list ->
                Observable.fromIterable(list)
                    .map {
                        com.tompee.utilities.passwordmanager.model.Package(
                            it.loadLabel(context.packageManager).toString(),
                            it.packageName,
                            it.loadIcon(context.packageManager)
                        )
                    }
                    .toSortedList()
            }
    }

    override fun getPackageFromName(packageName: String): Single<Package> {
        return Single.fromCallable { context.packageManager.getApplicationInfo(packageName, 0) }
            .map {
                com.tompee.utilities.passwordmanager.model.Package(
                    it.loadLabel(context.packageManager).toString(),
                    it.packageName,
                    it.loadIcon(context.packageManager)
                )
            }
    }
}