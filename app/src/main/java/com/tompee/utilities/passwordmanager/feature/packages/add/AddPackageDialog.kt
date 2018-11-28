package com.tompee.utilities.passwordmanager.feature.packages.add

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseDialogFragment
import com.tompee.utilities.passwordmanager.databinding.DialogAddBinding
import com.tompee.utilities.passwordmanager.feature.packages.PackageViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class AddPackageDialog : BaseDialogFragment() {

    @Inject
    lateinit var factory: PackageViewModel.Factory

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val vm = ViewModelProviders.of(activity!!, factory)[PackageViewModel::class.java]
        val binding =
            DataBindingUtil.inflate<DialogAddBinding>(LayoutInflater.from(context), R.layout.dialog_add, null, false)

        vm.generateEmptyPassword()
        vm.currentPackage.observe(this, Observer {
            binding.title.text = it.name
            binding.subtitle.text = it.packageName
            binding.appIcon.setImageDrawable(it.icon)
        })

        vm.generatedPassword.observe(this, Observer {
            binding.passView.editText?.setText(it)
        })

        binding.negative.setOnClickListener {
            dismiss()
        }

        binding.neutral.setOnClickListener {
            vm.generatePassword()
        }

        binding.positive.setOnClickListener {
            if (binding.userView.text.isEmpty()) {
                binding.userView.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            if (binding.etPassword.text.toString().isEmpty()) {
                binding.etPassword.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            vm.saveCredential(binding.userView.text.toString(), binding.etPassword.text.toString())
            dismiss()
        }

        return AlertDialog.Builder(activity!!)
            .setView(binding.root)
            .setCancelable(false)
            .create()
    }
}