package com.tompee.utilities.passwordmanager.feature.splash

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.passwordmanager.base.BaseViewModel
import com.tompee.utilities.passwordmanager.core.biometric.Status
import com.tompee.utilities.passwordmanager.interactor.FingerprintInteractor
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class SplashViewModel private constructor(fingerprintInteractor: FingerprintInteractor, context: Context) :
    BaseViewModel<FingerprintInteractor>(fingerprintInteractor, context) {

    class Factory(private val fingerprintInteractor: FingerprintInteractor,
                  private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SplashViewModel(fingerprintInteractor, context) as T
        }
    }

    val isFingerprintSupported = MutableLiveData<Boolean>()
    val hasRegisteredFingerprint = MutableLiveData<Boolean>()
    val needsClose = MutableLiveData<Boolean>()
    val authenticateResult = MutableLiveData<Boolean>()

    init {
        subscriptions += interactor.isFingerprintSupported()
            .subscribeOn(Schedulers.io())
            .subscribe(isFingerprintSupported::postValue)

        subscriptions += interactor.hasFingerprintRegistered()
            .subscribeOn(Schedulers.io())
            .subscribe(hasRegisteredFingerprint::postValue)
    }

    fun finish() {
        needsClose.postValue(true)
    }

    fun startAuthentication() {
        subscriptions += interactor.authenticate()
            .subscribeOn(Schedulers.io())
            .subscribe { authenticateResult.postValue(it.type == Status.OK) }
    }
}