package com.tompee.utilities.passwordmanager.feature.backup

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tompee.utilities.passwordmanager.core.navigator.Navigator
import com.tompee.utilities.passwordmanager.dependency.scope.BackupScope
import com.tompee.utilities.passwordmanager.feature.backup.key.RegisterKeyDialog
import com.tompee.utilities.passwordmanager.interactor.BackupInteractor
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [BackupModule.Bindings::class])
class BackupModule {

    @Module
    interface Bindings {
        @ContributesAndroidInjector
        fun bindRegisterKeyDialog(): RegisterKeyDialog
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
    fun provideBackupInteractor(): BackupInteractor = BackupInteractor()

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
        registerKeyDialog: RegisterKeyDialog
    ): BackupDialogManager =
        BackupDialogManager(fragmentManager, registerKeyDialog)

    @Provides
    @BackupScope
    fun provideRegisterKeyDialog(): RegisterKeyDialog = RegisterKeyDialog()
}