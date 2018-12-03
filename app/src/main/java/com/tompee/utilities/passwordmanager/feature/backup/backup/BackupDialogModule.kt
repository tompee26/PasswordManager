package com.tompee.utilities.passwordmanager.feature.backup.backup

import android.content.Context
import com.tompee.utilities.passwordmanager.core.navigator.Navigator
import com.tompee.utilities.passwordmanager.interactor.BackupInteractor
import dagger.Module
import dagger.Provides

@Module
class BackupDialogModule {
    @Provides
    fun provideBackupDialogViewModelFactory(
        backupInteractor: BackupInteractor,
        context: Context,
        navigator: Navigator
    ): BackupDialogViewModel.Factory =
        BackupDialogViewModel.Factory(backupInteractor, context, navigator)
}