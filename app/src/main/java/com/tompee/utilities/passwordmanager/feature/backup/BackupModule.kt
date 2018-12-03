package com.tompee.utilities.passwordmanager.feature.backup

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.database.PackageDao
import com.tompee.utilities.passwordmanager.core.database.SiteDao
import com.tompee.utilities.passwordmanager.core.datastore.DataStore
import com.tompee.utilities.passwordmanager.core.navigator.Navigator
import com.tompee.utilities.passwordmanager.dependency.scope.BackupScope
import com.tompee.utilities.passwordmanager.feature.backup.backup.BackupDialog
import com.tompee.utilities.passwordmanager.feature.backup.key.RegisterKeyDialog
import com.tompee.utilities.passwordmanager.interactor.BackupInteractor
import com.tompee.utilities.passwordmanager.model.UserContainer
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [BackupModule.Bindings::class])
class BackupModule {

    @Module
    interface Bindings {
        @ContributesAndroidInjector
        fun bindRegisterKeyDialog(): RegisterKeyDialog

        @ContributesAndroidInjector
        fun bindBackupDialog(): BackupDialog
    }

    @Provides
    @BackupScope
    fun provideBackupViewModelFactory(
        backupInteractor: BackupInteractor,
        context: Context,
        navigator: Navigator,
        backupDialogManager: BackupDialogManager
    ): BackupViewModel.Factory =
        BackupViewModel.Factory(backupInteractor, context, navigator, backupDialogManager)

    @Provides
    @BackupScope
    fun provideBackupInteractor(
        cipher: Cipher,
        dataStore: DataStore,
        packageDao: PackageDao,
        siteDao: SiteDao,
        userContainer: UserContainer
    ): BackupInteractor = BackupInteractor(cipher, dataStore, packageDao, siteDao, userContainer)

    @Provides
    @BackupScope
    fun provideNavigator(backupActivity: BackupActivity): Navigator = Navigator(backupActivity)

    @Provides
    @BackupScope
    fun provideFragmentManager(backupActivity: BackupActivity): FragmentManager = backupActivity.supportFragmentManager

    @Provides
    @BackupScope
    fun provideDialogManager(
        fragmentManager: FragmentManager,
        registerKeyDialog: RegisterKeyDialog,
        backupDialog: BackupDialog
    ): BackupDialogManager =
        BackupDialogManager(fragmentManager, registerKeyDialog, backupDialog)

    @Provides
    @BackupScope
    fun provideRegisterKeyDialog(): RegisterKeyDialog = RegisterKeyDialog()

    @Provides
    @BackupScope
    fun provideBackupDialog(): BackupDialog = BackupDialog()
}