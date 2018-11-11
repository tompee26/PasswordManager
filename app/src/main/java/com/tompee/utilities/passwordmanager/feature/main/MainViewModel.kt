package com.tompee.utilities.passwordmanager.feature.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseViewModel
import com.tompee.utilities.passwordmanager.interactor.MainInteractor

class MainViewModel private constructor(mainInteractor: MainInteractor, context: Context) :
    BaseViewModel<MainInteractor>(mainInteractor, context) {

    class Factory(private val mainInteractor: MainInteractor,
                  private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(mainInteractor, context) as T
        }
    }

    val title = MutableLiveData<String>()

    init {
        title.postValue(context.getString(R.string.title_applications))
    }
}