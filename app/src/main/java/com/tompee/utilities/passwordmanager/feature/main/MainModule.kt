package com.tompee.utilities.passwordmanager.feature.main

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tompee.utilities.passwordmanager.core.database.PackageDao
import com.tompee.utilities.passwordmanager.core.packages.PackageManager
import com.tompee.utilities.passwordmanager.dependency.scope.MainScope
import com.tompee.utilities.passwordmanager.feature.main.apps.AppFragment
import com.tompee.utilities.passwordmanager.feature.main.apps.AppModule
import com.tompee.utilities.passwordmanager.feature.main.sites.SitesFragment
import com.tompee.utilities.passwordmanager.feature.main.sites.SitesModule
import com.tompee.utilities.passwordmanager.interactor.MainInteractor
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [MainModule.Bindings::class])
class MainModule {

    @Module
    interface Bindings {

        @ContributesAndroidInjector(modules = [AppModule::class])
        fun bindAppFragment(): AppFragment

        @ContributesAndroidInjector(modules = [SitesModule::class])
        fun bindSitesFragment(): SitesFragment
    }

    @Provides
    @MainScope
    fun provideMainViewModelFactory(mainInteractor: MainInteractor, context: Context): MainViewModel.Factory =
        MainViewModel.Factory(mainInteractor, context)

    @Provides
    @MainScope
    fun provideMainInteractor(packageDao: PackageDao, packageManager: PackageManager): MainInteractor =
        MainInteractor(packageDao, packageManager)

    @Provides
    @MainScope
    fun provideFragmentManager(activity: MainActivity): FragmentManager = activity.supportFragmentManager

    @Provides
    @MainScope
    fun provideMainViewPagerAdapter(
        fragmentManager: FragmentManager,
        context: Context,
        appFragment: AppFragment,
        sitesFragment: SitesFragment
    ): MainViewPagerAdapter = MainViewPagerAdapter(fragmentManager, context, appFragment, sitesFragment)

    @Provides
    @MainScope
    fun provideAppFragment(): AppFragment = AppFragment()

    @Provides
    @MainScope
    fun provideSitesFragment(): SitesFragment = SitesFragment()
}