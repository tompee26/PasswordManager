package com.tompee.utilities.passwordmanager.core.biometric.reprint

import com.github.ajalt.reprint.core.AuthenticationResult
import com.github.ajalt.reprint.core.Reprint
import com.github.ajalt.reprint.rxjava2.RxReprint
import com.tompee.utilities.passwordmanager.core.biometric.Result
import com.tompee.utilities.passwordmanager.core.biometric.BiometricManager
import com.tompee.utilities.passwordmanager.core.biometric.Status
import io.reactivex.Flowable
import io.reactivex.Single

class ReprintBiometricManager() : BiometricManager {
    override fun isSupported(): Single<Boolean> = Single.fromCallable { Reprint.isHardwarePresent() }

    override fun hasRegisteredFingerprints(): Single<Boolean> = Single.fromCallable { Reprint.hasFingerprintRegistered() }

    override fun authenticate(): Flowable<Result> = RxReprint.authenticate()
            .map { res ->
                when (res.status) {
                    AuthenticationResult.Status.SUCCESS -> Result(Status.OK, "")
                    AuthenticationResult.Status.NONFATAL_FAILURE -> Result(Status.NON_FATAL_ERROR, res.errorMessage.toString())
                    AuthenticationResult.Status.FATAL_FAILURE -> Result(Status.FATAL_ERROR, res.errorMessage?.toString() ?: "")
                    else -> Result(Status.UNKNOWN, "")
                }
            }.doFinally{Reprint.cancelAuthentication()}
}