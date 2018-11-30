package com.tompee.utilities.passwordmanager.feature.auth

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.passwordmanager.Constants
import com.tompee.utilities.passwordmanager.base.BaseViewModel
import com.tompee.utilities.passwordmanager.core.navigator.Navigator
import com.tompee.utilities.passwordmanager.feature.common.ObservableString
import com.tompee.utilities.passwordmanager.feature.common.observe
import com.tompee.utilities.passwordmanager.interactor.BackupInteractor
import io.reactivex.Completable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.plusAssign
import java.util.concurrent.TimeUnit

class LoginViewModel private constructor(
    backupInteractor: BackupInteractor,
    context: Context,
    navigator: Navigator
) :
    BaseViewModel<BackupInteractor>(backupInteractor, context, navigator) {

    class Factory(
        private val backupInteractor: BackupInteractor,
        private val context: Context,
        private val navigator: Navigator
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(backupInteractor, context, navigator) as T
        }
    }

    enum class InputError {
        EMAIL_EMPTY,
        EMAIL_INVALID,
        EMAIL_OK,
        PASSWORD_EMPTY,
        PASSWORD_SHORT,
        PASSWORD_OK,
    }

    val username = ObservableString("")
    val password = ObservableString("")

    val switchPage = MutableLiveData<Int>()
    val emailError = MutableLiveData<InputError>()
    val passwordError = MutableLiveData<InputError>()
    val commandEnable = MutableLiveData<Boolean>()
    val commandError = MutableLiveData<String>()
    val processing = MutableLiveData<Boolean>()

    init {
        val usernameState = username.observe()
            .map {
                when {
                    it.isEmpty() -> InputError.EMAIL_EMPTY
                    !Patterns.EMAIL_ADDRESS.matcher(it).matches() -> InputError.EMAIL_INVALID
                    else -> InputError.EMAIL_OK
                }
            }
        val passwordState = password.observe()
            .map {
                when {
                    it.isEmpty() -> InputError.PASSWORD_EMPTY
                    it.length < Constants.MIN_PASS_COUNT -> InputError.PASSWORD_SHORT
                    else -> InputError.PASSWORD_OK
                }
            }

        subscriptions += Observables.combineLatest(usernameState, passwordState) { user, pass ->
            user == InputError.EMAIL_OK && pass == InputError.PASSWORD_OK
        }.subscribe(commandEnable::postValue)
        subscriptions += usernameState
            .debounce(1, TimeUnit.SECONDS)
            .subscribe(emailError::postValue)
        subscriptions += passwordState
            .debounce(1, TimeUnit.SECONDS)
            .subscribe(passwordError::postValue)
    }

    fun pageSwitch(page: Int) {
        switchPage.postValue(page)
    }

    fun startLogin() {
        subscriptions += Completable.fromAction { processing.postValue(true) }
            .andThen(interactor.login(username.get()!!, password.get()!!))
            .doFinally { processing.postValue(false) }
            .subscribe({}) { commandError.postValue(it.message) }
    }

    fun startSignup() {
        subscriptions += Completable.fromAction { processing.postValue(true) }
            .andThen(interactor.signup(username.get()!!, password.get()!!))
            .doFinally { processing.postValue(false) }
            .subscribe({}) { commandError.postValue(it.message) }
    }
}