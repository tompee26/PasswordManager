package com.tompee.utilities.passwordmanager.feature.backup.restore

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

class RestoreViewModel private constructor(
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
            return RestoreViewModel(backupInteractor, context, navigator) as T
        }
    }

    val restoreOngoing = MutableLiveData<Boolean>()
    val restoreError = MutableLiveData<String>()
    val restoreFinished = MutableLiveData<Boolean>()

    fun restore(key: String) {
        subscriptions += Completable.fromAction {
            restoreOngoing.postValue(true)
            restoreError.postValue("")
        }
            .andThen(
                interactor.restore(key)
                    .subscribeOn(Schedulers.computation())
            )
            .subscribeOn(Schedulers.io())
            .subscribe({
                restoreOngoing.postValue(false)
                restoreFinished.postValue(true)
            }) {
                restoreOngoing.postValue(false)
                restoreError.postValue(it.message)
            }
    }
}