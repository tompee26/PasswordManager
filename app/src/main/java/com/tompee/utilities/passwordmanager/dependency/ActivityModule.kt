package com.tompee.utilities.passwordmanager.dependency

import com.tompee.utilities.passwordmanager.dependency.scope.MainScope
import com.tompee.utilities.passwordmanager.dependency.scope.SplashScope
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
}
