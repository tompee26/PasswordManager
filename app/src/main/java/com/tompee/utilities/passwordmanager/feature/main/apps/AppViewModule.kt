package com.tompee.utilities.passwordmanager.feature.main.apps

import dagger.Module
import dagger.Provides

@Module
class AppViewModule {

    @Provides
    fun provideAppAdapter(): AppAdapter = AppAdapter()
}