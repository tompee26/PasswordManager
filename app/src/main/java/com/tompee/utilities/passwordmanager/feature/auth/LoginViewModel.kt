package com.tompee.utilities.passwordmanager.feature.auth

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.passwordmanager.base.BaseViewModel
import com.tompee.utilities.passwordmanager.interactor.BackupInteractor

class LoginViewModel private constructor(backupInteractor: BackupInteractor, context: Context) :
    BaseViewModel<BackupInteractor>(backupInteractor, context) {

    class Factory(
        private val backupInteractor: BackupInteractor,
        private val context: Context
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(backupInteractor, context) as T
        }
    }

    val switchPage = MutableLiveData<Int>()

    fun pageSwitch(page: Int) {
        switchPage.postValue(page)
    }
}