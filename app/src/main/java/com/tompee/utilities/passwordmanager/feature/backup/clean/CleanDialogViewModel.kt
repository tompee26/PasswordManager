package com.tompee.utilities.passwordmanager.feature.backup.clean

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

class CleanDialogViewModel private constructor(
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
            return CleanDialogViewModel(backupInteractor, context, navigator) as T
        }
    }

    val cleanOngoing = MutableLiveData<Boolean>()
    val cleanError = MutableLiveData<String>()
    val cleanFinished = MutableLiveData<Boolean>()

    fun clean(key: String) {
        subscriptions += Completable.fromAction {
            cleanOngoing.postValue(true)
            cleanError.postValue("")
        }
            .andThen(
                interactor.clean(key)
                    .subscribeOn(Schedulers.io())
            )
            .subscribeOn(Schedulers.io())
            .subscribe({
                cleanOngoing.postValue(false)
                cleanFinished.postValue(true)
            }) {
                cleanOngoing.postValue(false)
                cleanError.postValue(it.message)
            }
    }
}