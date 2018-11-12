package com.tompee.utilities.passwordmanager.feature.license

import android.content.Context
import com.tompee.utilities.passwordmanager.core.asset.AssetManager
import com.tompee.utilities.passwordmanager.dependency.scope.LicenseScope
import com.tompee.utilities.passwordmanager.interactor.AssetInteractor
import dagger.Module
import dagger.Provides

@Module
class LicenseModule {

    @Provides
    @LicenseScope
    fun provideLicenseViewModelFactory(assetInteractor: AssetInteractor, context: Context): LicenseViewModel.Factory =
        LicenseViewModel.Factory(assetInteractor, context)

    @Provides
    @LicenseScope
    fun provideAssetInteractor(assetManager: AssetManager): AssetInteractor = AssetInteractor(assetManager)

}
