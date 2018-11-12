package com.tompee.utilities.passwordmanager.feature.packages

import android.content.Context
import com.tompee.utilities.passwordmanager.core.packages.PackageManager
import com.tompee.utilities.passwordmanager.dependency.scope.PackageScope
import com.tompee.utilities.passwordmanager.feature.packages.add.AddPackageDialog
import com.tompee.utilities.passwordmanager.interactor.PackageInteractor
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [PackageModule.Bindings::class])
class PackageModule {

    @Module
    interface Bindings {
        @ContributesAndroidInjector
        fun bindAddPackageDialog() : AddPackageDialog
    }

    @Provides
    @PackageScope
    fun providePackageViewModelFactory(packageInteractor: PackageInteractor, context: Context) : PackageViewModel.Factory =
            PackageViewModel.Factory(packageInteractor, context)

    @Provides
    @PackageScope
    fun providePackageInteractor(packageManager: PackageManager) : PackageInteractor = PackageInteractor(packageManager)

    @Provides
    @PackageScope
    fun providePackageAdapter() : PackageAdapter = PackageAdapter()
}