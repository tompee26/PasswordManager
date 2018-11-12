package com.tompee.utilities.passwordmanager.feature.about

import android.content.Context
import com.tompee.utilities.passwordmanager.core.asset.AssetManager
import com.tompee.utilities.passwordmanager.dependency.scope.AboutScope
import com.tompee.utilities.passwordmanager.interactor.AssetInteractor
import dagger.Module
import dagger.Provides

@Module
class AboutModule {

    @Provides
    @AboutScope
    fun provideAboutViewModelFactory(assetInteractor: AssetInteractor, context: Context): AboutViewModel.Factory =
        AboutViewModel.Factory(assetInteractor, context)

    @Provides
    @AboutScope
    fun provideAsseInteractor(assetManager: AssetManager): AssetInteractor = AssetInteractor(assetManager)
}
