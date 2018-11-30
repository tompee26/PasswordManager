package com.tompee.utilities.passwordmanager.feature.main.backup

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseDialogFragment
import com.tompee.utilities.passwordmanager.feature.main.MainViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class BackupDialog : BaseDialogFragment() {

    @Inject
    lateinit var factory: MainViewModel.Factory

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val vm = ViewModelProviders.of(activity!!, factory)[MainViewModel::class.java]
        return AlertDialog.Builder(activity!!)
            .setTitle(R.string.label_backup)
            .setMessage(R.string.message_backup)
            .setPositiveButton(R.string.control_proceed) { _, _ ->
                vm.moveToBackUpScreen()
                dismiss()
            }
            .setNegativeButton(R.string.control_cancel) { _, _ -> dismiss() }
            .setCancelable(false)
            .create()
    }
}