package com.tompee.utilities.passwordmanager.interactor

import com.tompee.utilities.passwordmanager.base.BaseInteractor
import com.tompee.utilities.passwordmanager.core.packages.PackageManager
import com.tompee.utilities.passwordmanager.model.Package
import io.reactivex.Single

class PackageInteractor(private val packageManager: PackageManager) : BaseInteractor {

    fun getPackageList(): Single<List<Package>> = packageManager.getPackages()
}