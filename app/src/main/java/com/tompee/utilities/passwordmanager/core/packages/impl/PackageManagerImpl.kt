package com.tompee.utilities.passwordmanager.core.packages.impl

import android.content.Context
import android.content.Intent
import com.tompee.utilities.passwordmanager.core.packages.PackageManager
import com.tompee.utilities.passwordmanager.model.Package
import io.reactivex.Observable
import io.reactivex.Single

class PackageManagerImpl(private val context: Context) : PackageManager {

    override fun getPackages(): Single<List<com.tompee.utilities.passwordmanager.model.Package>> {
        return Single.fromCallable {
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            return@fromCallable context.packageManager.queryIntentActivities(intent, 0)
        }
            .flatMap { list ->
                Observable.fromIterable(list)
                    .map {
                        com.tompee.utilities.passwordmanager.model.Package(
                            it.loadLabel(context.packageManager).toString(),
                            it.activityInfo.packageName,
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