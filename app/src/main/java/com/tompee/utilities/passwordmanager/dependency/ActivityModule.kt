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
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivityModule {

    @SplashScope
    @ContributesAndroidInjector(modules = [SplashModule::class])
    internal abstract fun bindSplashActivity(): SplashActivity

    @MainScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun bindMainActivity(): MainActivity

    @PackageScope
    @ContributesAndroidInjector(modules = [PackageModule::class])
    internal abstract fun bindPackageActivity(): PackageActivity

    @AboutScope
    @ContributesAndroidInjector(modules = [AboutModule::class])
    internal abstract fun bindAboutActivity(): AboutActivity

    @LicenseScope
    @ContributesAndroidInjector(modules = [LicenseModule::class])
    internal abstract fun bindLicenseActivity(): LicenseActivity
}
