package com.tompee.utilities.passwordmanager.dependency

import com.tompee.utilities.passwordmanager.dependency.scope.*
import com.tompee.utilities.passwordmanager.feature.about.AboutActivity
import com.tompee.utilities.passwordmanager.feature.about.AboutModule
import com.tompee.utilities.passwordmanager.feature.license.LicenseActivity
import com.tompee.utilities.passwordmanager.feature.license.LicenseModule
import com.tompee.utilities.passwordmanager.feature.packages.PackageActivity
import com.tompee.utilities.passwordmanager.feature.packages.PackageModule
import com.tompee.utilities.passwordmanager.feature.main.MainActivity
import com.tompee.utilities.passwordmanager.feature.main.MainModule
import com.tompee.utilities.passwordmanager.feature.splash.SplashActivity
import com.tompee.utilities.passwordmanager.feature.splash.SplashModule
import com.tompee.utilities.passwordmanager.service.FillService
import com.tompee.utilities.passwordmanager.service.FillServiceModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ServiceModule {

    @ServiceScope
    @ContributesAndroidInjector(modules = [FillServiceModule::class])
    internal abstract fun bindFillService(): FillService
}
