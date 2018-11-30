package com.tompee.utilities.passwordmanager.feature.license

import android.content.Context
import com.tompee.utilities.passwordmanager.core.asset.AssetManager
import com.tompee.utilities.passwordmanager.core.navigator.Navigator
import com.tompee.utilities.passwordmanager.dependency.scope.LicenseScope
import com.tompee.utilities.passwordmanager.interactor.AssetInteractor
import dagger.Module
import dagger.Provides

@Module
class LicenseModule {

    @Provides
    @LicenseScope
    fun provideLicenseViewModelFactory(
        assetInteractor: AssetInteractor,
        context: Context,
        navigator: Navigator
    ): LicenseViewModel.Factory =
        LicenseViewModel.Factory(assetInteractor, context, navigator)

    @Provides
    @LicenseScope
    fun provideAssetInteractor(assetManager: AssetManager): AssetInteractor = AssetInteractor(assetManager)

    @Provides
    @LicenseScope
    fun provideNavigator(licenseActivity: LicenseActivity): Navigator = Navigator(licenseActivity)
}
