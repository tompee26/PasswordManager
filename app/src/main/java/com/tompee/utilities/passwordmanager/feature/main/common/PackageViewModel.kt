package com.tompee.utilities.passwordmanager.feature.main.common

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.passwordmanager.base.BaseViewModel
import com.tompee.utilities.passwordmanager.core.navigator.Navigator
import com.tompee.utilities.passwordmanager.interactor.MainInteractor
import com.tompee.utilities.passwordmanager.model.Package
import com.tompee.utilities.passwordmanager.model.PackageCredential
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class PackageViewModel private constructor(
    mainInteractor: MainInteractor,
    context: Context,
    navigator: Navigator
) :
    BaseViewModel<MainInteractor>(mainInteractor, context, navigator) {

    class Factory(
        private val mainInteractor: MainInteractor,
        private val context: Context,
        private val navigator: Navigator
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return PackageViewModel(mainInteractor, context, navigator) as T
        }
    }

    val list = MutableLiveData<List<PackageCredential>>()
    val searching = MutableLiveData<Boolean>()
    val currentPackage = MutableLiveData<PackageCredential>()
    val copiedToClipboard = MutableLiveData<Boolean>()

    init {
        subscriptions += Completable.fromAction { searching.postValue(true) }
            .andThen(interactor.getPackageList())
            .doOnNext { searching.postValue(false) }
            .doFinally { searching.postValue(false) }
            .subscribeOn(Schedulers.computation())
            .subscribe(list::postValue)
    }

    fun setCurrentPackage(pack: PackageCredential) {
        currentPackage.postValue(pack)
    }

    fun copyToClipboard(text: String) {
        subscriptions += interactor.copyToClipboard(text)
            .doOnComplete { copiedToClipboard.postValue(true) }
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun saveCredential(username: String, password: String) {
        val pack = currentPackage.value!!
        subscriptions += interactor.savePackageCredential(
            Package(pack.name, pack.packageName, pack.icon),
            username,
            password
        )
            .subscribeOn(Schedulers.computation())
            .subscribe()
    }

    fun delete(site: PackageCredential) {
        subscriptions += interactor.delete(site)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}