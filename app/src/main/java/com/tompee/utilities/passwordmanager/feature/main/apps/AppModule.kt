package com.tompee.utilities.passwordmanager.feature.main.apps

import android.content.Context
import com.tompee.utilities.passwordmanager.interactor.MainInteractor
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideAppViewModelFactory(mainInteractor: MainInteractor,
                                   context: Context) : AppViewModel.Factory =
            AppViewModel.Factory(mainInteractor, context)
}