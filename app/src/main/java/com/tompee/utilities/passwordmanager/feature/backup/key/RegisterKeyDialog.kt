package com.tompee.utilities.passwordmanager.feature.backup.key

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseDialogFragment
import com.tompee.utilities.passwordmanager.databinding.DialogPreferenceBinding
import com.tompee.utilities.passwordmanager.feature.backup.BackupViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RegisterKeyDialog : BaseDialogFragment() {

    @Inject
    lateinit var factory: BackupViewModel.Factory

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val vm = ViewModelProviders.of(activity!!, factory)[BackupViewModel::class.java]
        val binding =
            DataBindingUtil.inflate<DialogPreferenceBinding>(
                LayoutInflater.from(context),
                R.layout.dialog_preference,
                null,
                false
            )
        binding.message.text = getString(R.string.message_key_dialog)
        return AlertDialog.Builder(activity!!)
            .setTitle(R.string.label_register_key)
            .setView(binding.root)
            .setCancelable(false)
            .setPositiveButton(R.string.control_ok) { _, _ ->
                vm.setKey(binding.edit.text.toString())
                dismiss()
            }
            .setNegativeButton(R.string.control_cancel) { _, _ -> dismiss() }
            .create()
    }
}