package com.tompee.utilities.passwordmanager.feature.backup.key

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseDialogFragment
import com.tompee.utilities.passwordmanager.databinding.DialogRegisterKeyBinding
import com.tompee.utilities.passwordmanager.feature.backup.BackupViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RegisterKeyDialog : BaseDialogFragment() {

    @Inject
    lateinit var factory: BackupViewModel.Factory

    lateinit var binding: DialogRegisterKeyBinding

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_register_key,
            null,
            false
        )
        return AlertDialog.Builder(activity!!)
            .setTitle(R.string.label_register_key)
            .setView(binding.root)
            .setCancelable(false)
            .setPositiveButton(R.string.control_ok) { _, _ -> }
            .setNegativeButton(R.string.control_cancel) { _, _ -> dismiss() }
            .create()
    }

    override fun onResume() {
        super.onResume()
        val d = dialog as AlertDialog
        val positiveButton = d.getButton(Dialog.BUTTON_POSITIVE) as Button
        positiveButton.setOnClickListener {
            val vm = ViewModelProviders.of(activity!!, factory)[BackupViewModel::class.java]
            if (binding.pass1.editText?.text.toString().isEmpty()) {
                binding.pass1.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            binding.pass1.error = null
            if (binding.pass2.editText?.text.toString().isEmpty()) {
                binding.pass2.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            binding.pass2.error = null
            if (binding.pass1.editText?.text.toString() != binding.pass2.editText?.text.toString()) {
                binding.pass2.error = getString(R.string.error_mismatch)
                return@setOnClickListener
            }
            binding.pass2.error = null
            vm.setKey(binding.pass1.editText?.text.toString())
            dismiss()
        }
    }
}