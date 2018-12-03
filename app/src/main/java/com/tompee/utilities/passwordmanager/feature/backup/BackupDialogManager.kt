package com.tompee.utilities.passwordmanager.feature.backup

import androidx.fragment.app.FragmentManager
import com.tompee.utilities.passwordmanager.feature.backup.key.RegisterKeyDialog

class BackupDialogManager(
    private val fragmentManager: FragmentManager,
    private val registerKeyDialog: RegisterKeyDialog
) {

    enum class Dialogs {
        REGISTER_KEY
    }

    fun showDialog(dialog: Dialogs) {
        when (dialog) {
            Dialogs.REGISTER_KEY -> registerKeyDialog.show(
                fragmentManager, Dialogs.REGISTER_KEY.toString()
            )
        }
    }
}
