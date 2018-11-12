package com.tompee.utilities.passwordmanager.core.packages

import com.tompee.utilities.passwordmanager.model.Package
import io.reactivex.Single

interface PackageManager {
    fun getPackages(): Single<List<Package>>
}
