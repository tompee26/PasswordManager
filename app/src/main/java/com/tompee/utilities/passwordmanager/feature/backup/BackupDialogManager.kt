package com.tompee.utilities.passwordmanager.feature.backup

import androidx.fragment.app.FragmentManager
import com.tompee.utilities.passwordmanager.feature.backup.backup.BackupDialog
import com.tompee.utilities.passwordmanager.feature.backup.key.RegisterKeyDialog

class BackupDialogManager(
    private val fragmentManager: FragmentManager,
    private val registerKeyDialog: RegisterKeyDialog,
    private val backupDialog: BackupDialog
) {

    enum class Dialogs {
        REGISTER_KEY,
        BACKUP
    }

    fun showDialog(dialog: Dialogs) {
        when (dialog) {
            Dialogs.REGISTER_KEY -> registerKeyDialog.show(
                fragmentManager, Dialogs.REGISTER_KEY.toString()
            )
            Dialogs.BACKUP-> backupDialog.show(
                fragmentManager, Dialogs.BACKUP.toString()
            )
        }
    }
}
