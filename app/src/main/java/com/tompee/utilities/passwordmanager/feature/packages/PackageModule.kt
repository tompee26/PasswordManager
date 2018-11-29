package com.tompee.utilities.passwordmanager.feature.packages

import android.content.Context
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.database.PackageDao
import com.tompee.utilities.passwordmanager.core.generator.PasswordGenerator
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
        fun bindAddPackageDialog(): AddPackageDialog
    }

    @Provides
    @PackageScope
    fun providePackageViewModelFactory(
        packageInteractor: PackageInteractor,
        context: Context
    ): PackageViewModel.Factory =
        PackageViewModel.Factory(packageInteractor, context)

    @Provides
    @PackageScope
    fun providePackageInteractor(
        context: Context,
        packageManager: PackageManager,
        cipher: Cipher,
        packageDao: PackageDao,
        passwordGenerator: PasswordGenerator
    ):
            PackageInteractor =
        PackageInteractor(context, packageManager, cipher, packageDao, passwordGenerator)

    @Provides
    @PackageScope
    fun providePackageAdapter(): PackageAdapter = PackageAdapter()
}