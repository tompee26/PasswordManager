package com.tompee.utilities.passwordmanager.feature.splash

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.passwordmanager.base.BaseViewModel
import com.tompee.utilities.passwordmanager.core.biometric.Status
import com.tompee.utilities.passwordmanager.interactor.SplashInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class SplashViewModel private constructor(splashInteractor: SplashInteractor, context: Context) :
    BaseViewModel<SplashInteractor>(splashInteractor, context) {

    class Factory(
        private val splashInteractor: SplashInteractor,
        private val context: Context
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SplashViewModel(splashInteractor, context) as T
        }
    }

    val isFingerprintSupported = MutableLiveData<Boolean>()
    val hasRegisteredFingerprint = MutableLiveData<Boolean>()
    val needsClose = MutableLiveData<Boolean>()
    val authenticateResult = MutableLiveData<Boolean>()
    val autofillEnabled = MutableLiveData<Boolean>()

    init {
        subscriptions += interactor.isFingerprintSupported()
            .subscribeOn(Schedulers.io())
            .subscribe(isFingerprintSupported::postValue)

        subscriptions += interactor.hasFingerprintRegistered()
            .subscribeOn(Schedulers.io())
            .subscribe(hasRegisteredFingerprint::postValue)
    }

    fun finish(result: Boolean) {
        needsClose.postValue(result)
    }

    fun startAuthentication() {
        subscriptions += interactor.authenticate()
            .subscribeOn(Schedulers.io())
            .subscribe { authenticateResult.postValue(it.type == Status.OK) }
    }

    fun checkIfAutofillEnabled() {
        subscriptions += interactor.isAutofillEnabled()
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(autofillEnabled::postValue)
    }
}