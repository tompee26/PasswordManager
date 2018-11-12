package com.tompee.utilities.passwordmanager.feature.packages

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseViewModel
import com.tompee.utilities.passwordmanager.interactor.PackageInteractor
import com.tompee.utilities.passwordmanager.model.Package
import io.reactivex.Completable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class PackageViewModel private constructor(packageInteractor: PackageInteractor, context: Context) :
    BaseViewModel<PackageInteractor>(packageInteractor, context) {

    class Factory(
        private val packageInteractor: PackageInteractor,
        private val context: Context
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return PackageViewModel(packageInteractor, context) as T
        }
    }

    val title = MutableLiveData<String>()
    val packageList = MutableLiveData<List<Package>>()
    val searching = MutableLiveData<Boolean>()
    val currentPackage = MutableLiveData<Package>()

    init {
        title.postValue(context.getString(R.string.title_packages))
        subscriptions += Completable.fromAction { searching.postValue(true) }
            .andThen(interactor.getPackageList())
            .subscribeOn(Schedulers.io())
            .doFinally { searching.postValue(false) }
            .subscribe(packageList::postValue)
    }

    fun setCurrentPackage(pack : Package) {
        currentPackage.postValue(pack)
    }
}