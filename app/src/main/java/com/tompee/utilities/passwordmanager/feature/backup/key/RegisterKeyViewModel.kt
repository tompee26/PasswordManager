package com.tompee.utilities.passwordmanager.feature.backup.key

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.passwordmanager.base.BaseViewModel
import com.tompee.utilities.passwordmanager.core.navigator.Navigator
import com.tompee.utilities.passwordmanager.interactor.BackupInteractor
import io.reactivex.Completable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class RegisterKeyViewModel private constructor(
    backupInteractor: BackupInteractor,
    context: Context,
    navigator: Navigator
) : BaseViewModel<BackupInteractor>(backupInteractor, context, navigator) {

    class Factory(
        private val backupInteractor: BackupInteractor,
        private val context: Context,
        private val navigator: Navigator
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return RegisterKeyViewModel(backupInteractor, context, navigator) as T
        }
    }

    val keyAvailable = MutableLiveData<Boolean>()
    val registerOngoing = MutableLiveData<Boolean>()

    init {
        subscriptions += interactor.getEncryptedIdentifier()
            .subscribeOn(Schedulers.io())
            .subscribe { keyAvailable.postValue(it.isNotEmpty()) }
    }

    fun setKey(key: String) {
        subscriptions += Completable.fromAction { registerOngoing.postValue(true) }
            .andThen(
                interactor.saveEncryptedIdentifier(key)
                    .subscribeOn(Schedulers.computation())
            )
            .subscribeOn(Schedulers.io())
            .subscribe { registerOngoing.postValue(false) }
    }
}