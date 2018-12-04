package com.tompee.utilities.passwordmanager.feature.backup.key

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseDialogFragment
import com.tompee.utilities.passwordmanager.databinding.DialogRegisterKeyBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RegisterKeyDialog : BaseDialogFragment() {

    @Inject
    lateinit var factory: RegisterKeyViewModel.Factory

    lateinit var binding: DialogRegisterKeyBinding

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val vm = ViewModelProviders.of(this, factory)[RegisterKeyViewModel::class.java]
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_register_key,
            null,
            false
        )
        vm.keyAvailable.observe(this, Observer {
            if (it) dismiss()
        })
        vm.registerOngoing.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.textView.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.textView.visibility = View.GONE
            }
            binding.pass1.isEnabled = !it
            binding.pass2.isEnabled = !it
        })
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
            val vm = ViewModelProviders.of(this, factory)[RegisterKeyViewModel::class.java]
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
        }
    }
}