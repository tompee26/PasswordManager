package com.tompee.utilities.passwordmanager.interactor

import com.tompee.utilities.passwordmanager.base.BaseInteractor
import com.tompee.utilities.passwordmanager.core.biometric.BiometricManager


class FingerprintInteractor(private val biometricManager: BiometricManager) : BaseInteractor {

    fun isFingerprintSupported() = biometricManager.isSupported()

    fun hasFingerprintRegistered() = biometricManager.hasRegisteredFingerprints()

    fun authenticate() = biometricManager.authenticate()
}