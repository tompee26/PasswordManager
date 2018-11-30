package com.tompee.utilities.passwordmanager.feature.license

import android.content.Context
import android.text.Spanned
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.passwordmanager.Constants
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseViewModel
import com.tompee.utilities.passwordmanager.core.navigator.Navigator
import com.tompee.utilities.passwordmanager.interactor.AssetInteractor
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class LicenseViewModel private constructor(assetInteractor: AssetInteractor,
                                           context: Context,
                                           navigator: Navigator) :
    BaseViewModel<AssetInteractor>(assetInteractor, context, navigator) {

    class Factory(private val assetInteractor: AssetInteractor,
                  private val context: Context,
                  private val navigator: Navigator) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return LicenseViewModel(assetInteractor, context, navigator) as T
        }
    }

    val title = MutableLiveData<String>()
    val message = MutableLiveData<Spanned>()

    fun onLoaded(isLicense: Boolean) {
        subscriptions += if (isLicense) {
            title.postValue(context.getString(R.string.label_license))
            interactor.getStringFromAsset(Constants.LICENSE_ASSET)
                .subscribeOn(Schedulers.io())
                .subscribe(message::postValue)
        } else {
            title.postValue(context.getString(R.string.label_policy))
            interactor.getStringFromAsset(Constants.POLICY_ASSET)
                .subscribeOn(Schedulers.io())
                .subscribe(message::postValue)
        }
    }
}