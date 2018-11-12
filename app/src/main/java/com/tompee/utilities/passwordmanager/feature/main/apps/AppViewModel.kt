package com.tompee.utilities.passwordmanager.feature.main.apps

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.passwordmanager.base.BaseViewModel
import com.tompee.utilities.passwordmanager.interactor.MainInteractor

class AppViewModel private constructor(mainInteractor: MainInteractor, context: Context) :
    BaseViewModel<MainInteractor>(mainInteractor, context) {

    class Factory(private val mainInteractor: MainInteractor, private val context: Context) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(mainInteractor, context) as T
        }
    }
}