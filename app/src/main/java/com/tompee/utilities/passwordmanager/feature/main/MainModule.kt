package com.tompee.utilities.passwordmanager.feature.main

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.clipboard.ClipboardManager
import com.tompee.utilities.passwordmanager.core.database.PackageDao
import com.tompee.utilities.passwordmanager.core.database.SiteDao
import com.tompee.utilities.passwordmanager.core.generator.PasswordGenerator
import com.tompee.utilities.passwordmanager.core.keystore.Keystore
import com.tompee.utilities.passwordmanager.core.packages.PackageManager
import com.tompee.utilities.passwordmanager.dependency.scope.MainScope
import com.tompee.utilities.passwordmanager.feature.main.addsites.AddSitesDialog
import com.tompee.utilities.passwordmanager.feature.main.apps.AppFragment
import com.tompee.utilities.passwordmanager.feature.main.apps.AppViewModule
import com.tompee.utilities.passwordmanager.feature.main.common.PackageViewModel
import com.tompee.utilities.passwordmanager.feature.main.common.SitesViewModel
import com.tompee.utilities.passwordmanager.feature.main.sites.SitesFragment
import com.tompee.utilities.passwordmanager.feature.main.sites.SitesModule
import com.tompee.utilities.passwordmanager.feature.main.view.ViewPackageDialog
import com.tompee.utilities.passwordmanager.feature.main.view.ViewSitesDialog
import com.tompee.utilities.passwordmanager.interactor.MainInteractor
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [MainModule.Bindings::class])
class MainModule {

    @Module
    interface Bindings {

        @ContributesAndroidInjector(modules = [AppViewModule::class])
        fun bindAppFragment(): AppFragment

        @ContributesAndroidInjector(modules = [SitesModule::class])
        fun bindSitesFragment(): SitesFragment

        @ContributesAndroidInjector
        fun bindAddSitesDialog(): AddSitesDialog

        @ContributesAndroidInjector
        fun bindViewPackageDialog(): ViewPackageDialog

        @ContributesAndroidInjector
        fun bindViewSitesDialog(): ViewSitesDialog
    }

    @Provides
    @MainScope
    fun provideMainViewModelFactory(mainInteractor: MainInteractor, context: Context): MainViewModel.Factory =
        MainViewModel.Factory(mainInteractor, context)

    @Provides
    @MainScope
    fun provideMainInteractor(
        packageDao: PackageDao, siteDao: SiteDao, packageManager: PackageManager,
        keystore: Keystore, cipher: Cipher, context: Context, clipboardManager: ClipboardManager,
        passwordGenerator: PasswordGenerator
    ): MainInteractor =
        MainInteractor(packageDao, siteDao, packageManager, keystore, cipher, context, clipboardManager, passwordGenerator)

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
    fun providePackageViewModelFactory(
        mainInteractor: MainInteractor,
        context: Context
    ): PackageViewModel.Factory = PackageViewModel.Factory(mainInteractor, context)

    @Provides
    fun provideSitesViewModelFactory(
        mainInteractor: MainInteractor,
        context: Context
    ): SitesViewModel.Factory =
        SitesViewModel.Factory(mainInteractor, context)

    @Provides
    @MainScope
    fun provideAppFragment(): AppFragment = AppFragment()

    @Provides
    @MainScope
    fun provideSitesFragment(): SitesFragment = SitesFragment()
}