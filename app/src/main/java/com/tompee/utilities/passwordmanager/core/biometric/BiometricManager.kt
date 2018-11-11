package com.tompee.utilities.passwordmanager.core.biometric

import io.reactivex.Flowable
import io.reactivex.Single

interface BiometricManager {
    fun isSupported() : Single<Boolean>

    fun hasRegisteredFingerprints(): Single<Boolean>

    fun authenticate() : Flowable<Result>
}