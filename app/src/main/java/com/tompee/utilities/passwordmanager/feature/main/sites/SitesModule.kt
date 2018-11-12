package com.tompee.utilities.passwordmanager.feature.main.sites

import android.content.Context
import com.tompee.utilities.passwordmanager.interactor.MainInteractor
import dagger.Module
import dagger.Provides

@Module
class SitesModule {

    @Provides
    fun provideSitesViewModelFactory(mainInteractor: MainInteractor,
                                   context: Context) : SitesViewModel.Factory =
        SitesViewModel.Factory(mainInteractor, context)
}