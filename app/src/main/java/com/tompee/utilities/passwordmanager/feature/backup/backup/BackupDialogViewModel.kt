package com.tompee.utilities.passwordmanager.feature.backup.backup

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

class BackupDialogViewModel private constructor(
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
            return BackupDialogViewModel(backupInteractor, context, navigator) as T
        }
    }

    val backupOngoing = MutableLiveData<Boolean>()
    val backupError = MutableLiveData<String>()
    val backupFinished = MutableLiveData<Boolean>()

    fun proceedWithBackup(key: String) {
        subscriptions += Completable.fromAction {
            backupOngoing.postValue(true)
            backupError.postValue("")
        }
            .andThen(interactor.backup(key))
            .subscribeOn(Schedulers.io())
            .subscribe({
                backupOngoing.postValue(false)
                backupFinished.postValue(true)
            }) {
                backupOngoing.postValue(false)
                backupError.postValue(it.message)
            }
    }

}