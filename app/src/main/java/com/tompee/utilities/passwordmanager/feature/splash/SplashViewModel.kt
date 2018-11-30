package com.tompee.utilities.passwordmanager.feature.splash

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.passwordmanager.base.BaseViewModel
import com.tompee.utilities.passwordmanager.core.biometric.Status
import com.tompee.utilities.passwordmanager.core.navigator.Navigator
import com.tompee.utilities.passwordmanager.interactor.SplashInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class SplashViewModel private constructor(
    splashInteractor: SplashInteractor,
    context: Context,
    navigator: Navigator,
    private val splashDialogManager: SplashDialogManager
) :
    BaseViewModel<SplashInteractor>(splashInteractor, context, navigator) {

    class Factory(
        private val splashInteractor: SplashInteractor,
        private val context: Context,
        private val navigator: Navigator,
        private val splashDialogManager: SplashDialogManager
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SplashViewModel(splashInteractor, context, navigator, splashDialogManager) as T
        }
    }

    val needsClose = MutableLiveData<Boolean>()
    val authenticateResult = MutableLiveData<Boolean>()

    init {
        subscriptions += interactor.isFingerprintSupported()
            .filter { !it }
            .map { SplashDialogManager.Dialogs.FINGERPRINT_NOT_SUPPORTED }
            .mergeWith(interactor.hasFingerprintRegistered()
                .filter { !it }
                .map { SplashDialogManager.Dialogs.NO_REGISTERED_FINGERPRINTS })
            .first(SplashDialogManager.Dialogs.AUTHENTICATION)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(splashDialogManager::showDialog)
    }

    fun requestFinish(result: Boolean) {
        needsClose.postValue(result)
    }

    fun startAuthentication() {
        subscriptions += interactor.authenticate()
            .subscribeOn(Schedulers.io())
            .doOnNext { authenticateResult.postValue(it.type == Status.OK) }
            .filter { it.type == Status.OK }
            .flatMapSingle { interactor.isAutofillEnabled() }
            .subscribe { if (it) needsClose.postValue(it) else splashDialogManager.showDialog(SplashDialogManager.Dialogs.ENABLE_AUTOFILL) }
    }
}