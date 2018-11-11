package com.tompee.utilities.passwordmanager.feature.main

import android.content.Context
import com.tompee.utilities.passwordmanager.dependency.scope.MainScope
import com.tompee.utilities.passwordmanager.interactor.MainInteractor
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    @MainScope
    fun provideMainViewModelFactory(mainInteractor: MainInteractor,
                                    context: Context) : MainViewModel.Factory =
            MainViewModel.Factory(mainInteractor, context)

    @Provides
    @MainScope
    fun provideMainInteractor() : MainInteractor = MainInteractor()
}