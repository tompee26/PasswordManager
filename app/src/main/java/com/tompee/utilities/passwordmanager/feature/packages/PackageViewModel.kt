package com.tompee.utilities.passwordmanager.feature.packages

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseViewModel
import com.tompee.utilities.passwordmanager.core.navigator.Navigator
import com.tompee.utilities.passwordmanager.interactor.PackageInteractor
import com.tompee.utilities.passwordmanager.model.Package
import io.reactivex.Completable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class PackageViewModel private constructor(
    packageInteractor: PackageInteractor,
    context: Context,
    navigator: Navigator
) :
    BaseViewModel<PackageInteractor>(packageInteractor, context, navigator) {

    class Factory(
        private val packageInteractor: PackageInteractor,
        private val context: Context,
        private val navigator: Navigator
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return PackageViewModel(packageInteractor, context, navigator) as T
        }
    }

    val title = MutableLiveData<String>()
    val packageList = MutableLiveData<List<Package>>()
    val searching = MutableLiveData<Boolean>()
    val currentPackage = MutableLiveData<Package>()
    val generatedPassword = MutableLiveData<String>()

    init {
        title.postValue(context.getString(R.string.title_packages))
        subscriptions += Completable.fromAction { searching.postValue(true) }
            .andThen(interactor.getPackageList())
            .subscribeOn(Schedulers.io())
            .doOnNext { searching.postValue(false) }
            .doFinally { searching.postValue(false) }
            .subscribe(packageList::postValue)
    }

    fun setCurrentPackage(pack: Package) {
        currentPackage.postValue(pack)
    }

    fun saveCredential(username: String, password: String) {
        val pack = currentPackage.value!!
        subscriptions += interactor.savePackageCredential(pack, username, password)
            .subscribeOn(Schedulers.computation())
            .subscribe()
    }

    fun generatePassword() {
        subscriptions += interactor.generatePassword()
            .subscribeOn(Schedulers.computation())
            .subscribe(generatedPassword::postValue)
    }

    fun generateEmptyPassword() {
        generatedPassword.postValue("")
    }
}