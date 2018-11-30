package com.tompee.utilities.passwordmanager.feature.main.backup

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import com.tompee.utilities.passwordmanager.Constants
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseDialogFragment
import com.tompee.utilities.passwordmanager.feature.auth.LoginActivity
import dagger.android.support.AndroidSupportInjection

class BackupDialog : BaseDialogFragment() {

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity!!)
            .setTitle(R.string.label_backup)
            .setMessage(R.string.message_backup)
            .setPositiveButton(R.string.control_proceed) { _, _ ->
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                activity?.startActivityForResult(intent, Constants.REQUEST_CODE)
                dismiss()
            }
            .setNegativeButton(R.string.control_cancel) { _, _ -> dismiss() }
            .setCancelable(false)
            .create()
    }
}