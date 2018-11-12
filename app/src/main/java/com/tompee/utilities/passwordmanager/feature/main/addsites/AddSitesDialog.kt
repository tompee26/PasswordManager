package com.tompee.utilities.passwordmanager.feature.main.addsites

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
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

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.add.setOnClickListener {
            if (binding.siteNameView.text.isEmpty()) {
                binding.siteNameView.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            if (binding.siteUrlView.text.isEmpty()) {
                binding.siteUrlView.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            if (binding.userView.text.isEmpty()) {
                binding.userView.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            if (binding.etPassword.text.toString().isEmpty()) {
                binding.etPassword.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            vm.saveCredential(
                binding.siteNameView.text.toString(),
                binding.siteUrlView.text.toString(),
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