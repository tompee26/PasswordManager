package com.tompee.utilities.passwordmanager.feature.main

import android.content.Context
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseViewModel
import com.tompee.utilities.passwordmanager.core.navigator.*
import com.tompee.utilities.passwordmanager.feature.main.addsites.AddSitesDialog
import com.tompee.utilities.passwordmanager.feature.main.backup.BackupDialog
import com.tompee.utilities.passwordmanager.interactor.MainInteractor
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

class MainViewModel private constructor(
    mainInteractor: MainInteractor,
    context: Context,
    navigator: Navigator,
    private val fragmentManager: FragmentManager
) : BaseViewModel<MainInteractor>(mainInteractor, context, navigator) {

    class Factory(
        private val mainInteractor: MainInteractor,
        private val context: Context,
        private val navigator: Navigator,
        private val fragmentManager: FragmentManager
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(mainInteractor, context, navigator, fragmentManager) as T
        }
    }

    val title = MutableLiveData<String>()
    val generatedPassword = MutableLiveData<String>()

    init {
        title.postValue(context.getString(R.string.app_name))
    }

    fun addPackage() {
        navigator.setAddToBackStack(Package)
    }

    fun addSite() {
        val dialog = AddSitesDialog()
        dialog.show(fragmentManager, "addsites")
    }

    fun menuClicked(@IdRes id: Int): Boolean {
        return when (id) {
            R.id.menu_backup -> {
                val dialog = BackupDialog()
                dialog.show(fragmentManager, "backup")
                true
            }
            R.id.menu_about -> {
                navigator.setAddToBackStack(About)
                true
            }
            R.id.menu_policy -> {
                navigator.setAddToBackStack(Policy)
                true
            }
            R.id.menu_license -> {
                navigator.setAddToBackStack(License)
                true
            }
            else -> false
        }
    }

    fun saveCredential(siteName: String, siteUrl: String, username: String, password: String) {
        subscriptions += interactor.saveCredential(siteName, siteUrl, username, password)
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

    fun moveToBackUpScreen() {
        navigator.setAddToBackStack(Login)
    }
}