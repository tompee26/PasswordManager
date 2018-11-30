package com.tompee.utilities.passwordmanager.feature.backup

import android.content.Context
import com.tompee.utilities.passwordmanager.core.navigator.Navigator
import com.tompee.utilities.passwordmanager.dependency.scope.BackupScope
import com.tompee.utilities.passwordmanager.interactor.BackupInteractor
import dagger.Module
import dagger.Provides

@Module
class BackupModule {

    @Provides
    @BackupScope
    fun provideBackupViewModelFactory(
        backupInteractor: BackupInteractor,
        context: Context,
        navigator: Navigator
    ): BackupViewModel.Factory =
        BackupViewModel.Factory(backupInteractor, context, navigator)

    @Provides
    @BackupScope
    fun provideBackupInteractor(): BackupInteractor = BackupInteractor()

    @Provides
    @BackupScope
    fun provideNavigator(backupActivity: BackupActivity): Navigator = Navigator(backupActivity)
}