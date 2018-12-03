package com.tompee.utilities.passwordmanager.feature.main.addsites

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseDialogFragment
import com.tompee.utilities.passwordmanager.databinding.DialogAddSiteBinding
import com.tompee.utilities.passwordmanager.feature.main.MainViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class AddSitesDialog : BaseDialogFragment() {

    @Inject
    lateinit var factory: MainViewModel.Factory

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val vm = ViewModelProviders.of(activity!!, factory)[MainViewModel::class.java]
        val binding =
            DataBindingUtil.inflate<DialogAddSiteBinding>(
                LayoutInflater.from(context),
                R.layout.dialog_add_site,
                null,
                false
            )

        vm.generateEmptyPassword()
        vm.generatedPassword.observe(this, Observer {
            binding.sitePassword.editText?.setText(it)
        })

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.generate.setOnClickListener {
            vm.generatePassword()
        }

        binding.add.setOnClickListener {
            if (binding.siteName.editText?.text.toString().isEmpty()) {
                binding.siteName.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            binding.siteName.error = null
            if (binding.siteUrl.editText?.text.toString().isEmpty()) {
                binding.siteUrl.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            binding.siteUrl.error = null
            if (binding.siteUsername.editText?.text.toString().isEmpty()) {
                binding.siteUsername.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            binding.siteUsername.error = null
            if (binding.sitePassword.editText?.text.toString().isEmpty()) {
                binding.sitePassword.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            binding.siteName.error = null
            vm.saveCredential(
                binding.siteName.editText?.text.toString(),
                binding.siteUrl.editText?.text.toString(),
                binding.siteUsername.editText?.text.toString(),
                binding.sitePassword.editText?.text.toString()
            )
            dismiss()
        }

        return AlertDialog.Builder(activity!!)
            .setView(binding.root)
            .setCancelable(false)
            .create()
    }
}