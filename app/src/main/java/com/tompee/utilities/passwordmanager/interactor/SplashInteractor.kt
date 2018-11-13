package com.tompee.utilities.passwordmanager.interactor

import com.tompee.utilities.passwordmanager.base.BaseInteractor
import com.tompee.utilities.passwordmanager.core.autofill.AutofillManager
import com.tompee.utilities.passwordmanager.core.biometric.BiometricManager
import io.reactivex.Single


class SplashInteractor(private val biometricManager: BiometricManager,
                       private val autofillManager: AutofillManager) : BaseInteractor {

    fun isFingerprintSupported() = biometricManager.isSupported()

    fun hasFingerprintRegistered() = biometricManager.hasRegisteredFingerprints()

    fun authenticate() = biometricManager.authenticate()

    fun isAutofillEnabled(): Single<Boolean> = autofillManager.isEnabled()
}