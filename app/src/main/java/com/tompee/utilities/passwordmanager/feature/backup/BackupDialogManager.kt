package com.tompee.utilities.passwordmanager.feature.backup

import androidx.fragment.app.FragmentManager
import com.tompee.utilities.passwordmanager.feature.backup.backup.BackupDialog
import com.tompee.utilities.passwordmanager.feature.backup.clean.CleanDialog
import com.tompee.utilities.passwordmanager.feature.backup.key.RegisterKeyDialog
import com.tompee.utilities.passwordmanager.feature.backup.restore.RestoreDialog

class BackupDialogManager(private val fragmentManager: FragmentManager) {

    enum class Dialogs {
        REGISTER_KEY,
        BACKUP,
        RESTORE,
        CLEAN
    }

    fun showDialog(dialog: Dialogs) {
        when (dialog) {
            Dialogs.REGISTER_KEY -> {
                val registerKeyDialog = RegisterKeyDialog()
                registerKeyDialog.show(fragmentManager, Dialogs.REGISTER_KEY.toString())
            }
            Dialogs.BACKUP -> {
                val backupDialog = BackupDialog()
                backupDialog.show(fragmentManager, Dialogs.BACKUP.toString())
            }
            Dialogs.RESTORE -> {
                val restoreDialog = RestoreDialog()
                restoreDialog.show(fragmentManager, Dialogs.RESTORE.toString())
            }
            Dialogs.CLEAN -> {
                val cleanDialog = CleanDialog()
                cleanDialog.show(fragmentManager, Dialogs.CLEAN.toString())
            }
        }
    }
}
