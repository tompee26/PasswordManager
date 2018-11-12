package com.tompee.utilities.passwordmanager.feature.about

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.passwordmanager.BuildConfig
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseViewModel
import com.tompee.utilities.passwordmanager.interactor.AssetInteractor

class AboutViewModel private constructor(assetInteractor: AssetInteractor, context: Context) :
    BaseViewModel<AssetInteractor>(assetInteractor, context) {

    class Factory(private val assetInteractor: AssetInteractor, private val context: Context) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AboutViewModel(assetInteractor, context) as T
        }
    }

    val title = MutableLiveData<String>()
    val version = MutableLiveData<String>()

    init {
        title.postValue(context.getString(R.string.label_about))
        version.postValue(BuildConfig.VERSION_NAME)
    }
}