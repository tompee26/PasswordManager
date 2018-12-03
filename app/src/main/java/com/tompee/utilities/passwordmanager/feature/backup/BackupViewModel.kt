package com.tompee.utilities.passwordmanager.feature.backup

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseViewModel
import com.tompee.utilities.passwordmanager.core.navigator.Navigator
import com.tompee.utilities.passwordmanager.interactor.BackupInteractor
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class BackupViewModel private constructor(
    backupInteractor: BackupInteractor,
    context: Context,
    navigator: Navigator,
    private val dialogManager: BackupDialogManager
) :
    BaseViewModel<BackupInteractor>(backupInteractor, context, navigator) {

    class Factory(
        private val backupInteractor: BackupInteractor,
        private val context: Context,
        private val navigator: Navigator,
        private val dialogManager: BackupDialogManager
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return BackupViewModel(backupInteractor, context, navigator, dialogManager) as T
        }
    }

    val title = MutableLiveData<String>()
    val keyAvailable = MutableLiveData<Boolean>()

    init {
        title.postValue(context.getString(R.string.title_backup))
        subscriptions += interactor.getEncryptedIdentifier()
            .subscribeOn(Schedulers.io())
            .subscribe { keyAvailable.postValue(it.isNotEmpty()) }
    }

    fun showRegisterKeyDialog() {
        dialogManager.showDialog(BackupDialogManager.Dialogs.REGISTER_KEY)
    }

    fun setKey(key: String) {
        keyAvailable.postValue(true)
        subscriptions += interactor.saveEncryptedIdentifier(key)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}