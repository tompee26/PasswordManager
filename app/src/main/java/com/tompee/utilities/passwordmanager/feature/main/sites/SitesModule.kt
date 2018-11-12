package com.tompee.utilities.passwordmanager.feature.main.sites

import android.content.Context
import com.tompee.utilities.passwordmanager.interactor.MainInteractor
import dagger.Module
import dagger.Provides

@Module
class SitesModule {

    @Provides
    fun provideSiteAdapter(): SitesAdapter = SitesAdapter()
}