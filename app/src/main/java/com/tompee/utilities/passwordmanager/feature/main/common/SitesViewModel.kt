package com.tompee.utilities.passwordmanager.feature.main.common

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.passwordmanager.base.BaseViewModel
import com.tompee.utilities.passwordmanager.interactor.MainInteractor
import com.tompee.utilities.passwordmanager.model.PackageCredential
import com.tompee.utilities.passwordmanager.model.SiteCredential
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class SitesViewModel private constructor(mainInteractor: MainInteractor, context: Context) :
    BaseViewModel<MainInteractor>(mainInteractor, context) {

    class Factory(private val mainInteractor: MainInteractor, private val context: Context) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SitesViewModel(mainInteractor, context) as T
        }
    }

    val list = MutableLiveData<List<SiteCredential>>()
    val searching = MutableLiveData<Boolean>()
    val currentSite = MutableLiveData<SiteCredential>()
    val copiedToClipboard = MutableLiveData<Boolean>()

    init {
        subscriptions += Completable.fromAction { searching.postValue(true) }
            .andThen(interactor.getSiteList())
            .doOnNext { searching.postValue(false) }
            .doFinally { searching.postValue(false) }
            .subscribeOn(Schedulers.io())
            .subscribe(list::postValue)
    }

    fun setCurrentSite(site: SiteCredential) {
        currentSite.postValue(site)
    }

    fun copyToClipboard(text: String) {
        subscriptions += interactor.copyToClipboard(text)
            .doOnComplete { copiedToClipboard.postValue(true) }
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun saveCredential(siteName: String, siteUrl: String, username: String, password: String) {
        subscriptions += interactor.saveCredential(siteName, siteUrl, username, password)
            .subscribeOn(Schedulers.computation())
            .subscribe()
    }
}