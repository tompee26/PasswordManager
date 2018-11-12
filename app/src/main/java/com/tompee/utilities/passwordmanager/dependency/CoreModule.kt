package com.tompee.utilities.passwordmanager.dependency

import android.content.Context
import com.tompee.utilities.passwordmanager.core.biometric.BiometricManager
import com.tompee.utilities.passwordmanager.core.biometric.reprint.ReprintBiometricManager
import com.tompee.utilities.passwordmanager.core.packages.PackageManager
import com.tompee.utilities.passwordmanager.core.packages.impl.PackageManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreModule {

    @Provides
    @Singleton
    fun provideBiometricManager(reprintBiometricManager: ReprintBiometricManager): BiometricManager =
        reprintBiometricManager

    @Provides
    @Singleton
    fun provideReprintBiometricManager(): ReprintBiometricManager = ReprintBiometricManager()

    @Provides
    @Singleton
    fun providePackageManager(packageManagerImpl: PackageManagerImpl) : PackageManager = packageManagerImpl

    @Provides
    @Singleton
    fun providePackageManagerImpl(context: Context) : PackageManagerImpl = PackageManagerImpl(context)
}