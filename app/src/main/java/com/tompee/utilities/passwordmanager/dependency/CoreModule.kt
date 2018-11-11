package com.tompee.utilities.passwordmanager.dependency

import com.tompee.utilities.passwordmanager.core.biometric.BiometricManager
import com.tompee.utilities.passwordmanager.core.biometric.reprint.ReprintBiometricManager
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
}