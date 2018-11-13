package com.tompee.utilities.passwordmanager.feature.splash

import android.content.Context
import com.tompee.utilities.passwordmanager.core.autofill.AutofillManager
import com.tompee.utilities.passwordmanager.core.biometric.BiometricManager
import com.tompee.utilities.passwordmanager.dependency.scope.FingerprintScope
import com.tompee.utilities.passwordmanager.dependency.scope.SplashScope
import com.tompee.utilities.passwordmanager.feature.splash.activate.ActivateDialog
import com.tompee.utilities.passwordmanager.feature.splash.authenticate.FingerprintDialog
import com.tompee.utilities.passwordmanager.feature.splash.register.FingerprintRegisterDialog
import com.tompee.utilities.passwordmanager.feature.splash.support.FingerprintSupportDialog
import com.tompee.utilities.passwordmanager.interactor.SplashInteractor
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [SplashModule.Bindings::class])
class SplashModule {

    @Module
    interface Bindings {
        @FingerprintScope
        @ContributesAndroidInjector
        fun bindFingerprintDialog(): FingerprintDialog

        @ContributesAndroidInjector
        fun bindFingerprintSupportedDialog(): FingerprintSupportDialog

        @ContributesAndroidInjector
        fun bindFingerprintRegisterDialog(): FingerprintRegisterDialog

        @ContributesAndroidInjector
        fun bindActivateDialog(): ActivateDialog
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
    fun provideSplashViewModelFactory(
        splashInteractor: SplashInteractor,
        context: Context
    ): SplashViewModel.Factory =
        SplashViewModel.Factory(splashInteractor, context)

    @Provides
    @SplashScope
    fun provideFingerprintInteractor(biometricManager: BiometricManager, autofillManager: AutofillManager):
            SplashInteractor = SplashInteractor(biometricManager, autofillManager)
}