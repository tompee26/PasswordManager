package com.tompee.utilities.passwordmanager.feature.splash

import android.content.Context
import com.tompee.utilities.passwordmanager.core.biometric.BiometricManager
import com.tompee.utilities.passwordmanager.dependency.scope.FingerprintScope
import com.tompee.utilities.passwordmanager.dependency.scope.SplashScope
import com.tompee.utilities.passwordmanager.feature.splash.authenticate.FingerprintDialog
import com.tompee.utilities.passwordmanager.feature.splash.register.FingerprintRegisterDialog
import com.tompee.utilities.passwordmanager.feature.splash.support.FingerprintSupportDialog
import com.tompee.utilities.passwordmanager.interactor.FingerprintInteractor
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [SplashModule.Bindings::class])
class SplashModule {

    @Module
    interface Bindings {
        @FingerprintScope
        @ContributesAndroidInjector
        fun bindFingerprintDialog() : FingerprintDialog

        @ContributesAndroidInjector
        fun bindFingerprintSupportedDialog() : FingerprintSupportDialog

        @ContributesAndroidInjector
        fun bindFingerprintRegisterDialog() : FingerprintRegisterDialog
    }

    @Provides
    @SplashScope
    fun provideFingerprintDialog(): FingerprintDialog = FingerprintDialog()

    @Provides
    @SplashScope
    fun provideFingerprintSupportDialog(): FingerprintSupportDialog = FingerprintSupportDialog()

    @Provides
    @SplashScope
    fun provideFingerprintRegisterDialog(): FingerprintRegisterDialog = FingerprintRegisterDialog()

    @Provides
    @SplashScope
    fun provideSplashViewModelFactory(fingerprintInteractor: FingerprintInteractor,
                                      context: Context): SplashViewModel.Factory =
            SplashViewModel.Factory(fingerprintInteractor, context)

    @Provides
    @SplashScope
    fun provideFingerprintInteractor(biometricManager: BiometricManager) :
            FingerprintInteractor = FingerprintInteractor(biometricManager)
}