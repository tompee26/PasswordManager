package com.tompee.utilities.passwordmanager.feature.backup.key

import android.content.Context
import com.tompee.utilities.passwordmanager.core.navigator.Navigator
import com.tompee.utilities.passwordmanager.interactor.BackupInteractor
import dagger.Module
import dagger.Provides

@Module
class RegisterKeyModule {
    @Provides
    fun provideRestoreViewModelFactory(
        backupInteractor: BackupInteractor,
        context: Context,
        navigator: Navigator
    ): RegisterKeyViewModel.Factory =
        RegisterKeyViewModel.Factory(backupInteractor, context, navigator)
}