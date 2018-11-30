package com.tompee.utilities.passwordmanager.base

import android.content.Context
import androidx.lifecycle.ViewModel
import com.tompee.utilities.passwordmanager.core.navigator.Navigator
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel<T : BaseInteractor>(protected val interactor: T,
                                                 protected val context: Context,
                                                 protected val navigator: Navigator
) : ViewModel() {
    protected val subscriptions = CompositeDisposable()

    override fun onCleared() {
        subscriptions.clear()
        super.onCleared()
    }
}