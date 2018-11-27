package com.tompee.utilities.passwordmanager.feature.main.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseDialogFragment
import com.tompee.utilities.passwordmanager.databinding.DialogAddBinding
import com.tompee.utilities.passwordmanager.feature.main.common.PackageViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ViewPackageDialog : BaseDialogFragment() {

    @Inject
    lateinit var factory: PackageViewModel.Factory

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val vm = ViewModelProviders.of(activity!!, factory)[PackageViewModel::class.java]
        val binding =
            DataBindingUtil.inflate<DialogAddBinding>(LayoutInflater.from(context), R.layout.dialog_add, null, false)

        vm.currentPackage.observe(this, Observer {
            binding.title.text = it.name
            binding.subtitle.text = it.packageName
            binding.userView.isEnabled = false
            binding.userView.setText(it.username)
            binding.description.text = getString(R.string.message_view_credential)
            binding.etPassword.isEnabled = false
            binding.etPassword.setText(it.password)
            binding.appIcon.setImageDrawable(it.icon)

            binding.generate.text = getString(R.string.control_copy)
            binding.cancel.text = getString(R.string.control_edit)
            binding.add.text = getString(R.string.control_ok)
        })

        binding.cancel.setOnClickListener {
            binding.userView.isEnabled = true
            binding.passView.isEnabled = true
            binding.add.text = getString(R.string.control_save)
            binding.generate.visibility = View.GONE
            binding.cancel.text = getString(R.string.control_cancel)
            binding.cancel.setOnClickListener { dismiss() }
        }

        binding.generate.setOnClickListener{
            vm.copyToClipboard(binding.etPassword.text.toString())
        }

        binding.add.setOnClickListener {
            if (binding.userView.text.isEmpty()) {
                binding.userView.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            if (binding.etPassword.text.toString().isEmpty()) {
                binding.etPassword.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            vm.saveCredential(
                binding.userView.text.toString(),
                binding.etPassword.text.toString()
            )
            dismiss()
        }

        return AlertDialog.Builder(activity!!)
            .setView(binding.root)
            .setCancelable(false)
            .create()
    }
}