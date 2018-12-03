package com.tompee.utilities.passwordmanager.feature.main.view

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
import com.tompee.utilities.passwordmanager.feature.main.common.SitesViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ViewSitesDialog : BaseDialogFragment() {

    @Inject
    lateinit var factory: SitesViewModel.Factory

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val vm = ViewModelProviders.of(activity!!, factory)[SitesViewModel::class.java]
        val binding =
            DataBindingUtil.inflate<DialogAddBinding>(LayoutInflater.from(context), R.layout.dialog_add, null, false)

        vm.currentSite.observe(this, Observer {
            binding.title.text = it.name
            binding.subtitle.text = it.url

            binding.packUsername.isEnabled = false
            binding.packUsername.editText?.setText(it.username)

            binding.description.text = getString(R.string.message_view_credential)

            binding.packPassword.isEnabled = false
            binding.packPassword.editText?.setText(it.password)

            binding.neutral.text = getString(R.string.control_copy)
            binding.negative.text = getString(R.string.control_edit)
            binding.positive.text = getString(R.string.control_ok)
        })

        binding.negative.setOnClickListener {
            binding.packUsername.isEnabled = true
            binding.packPassword.isEnabled = true
            binding.positive.text = getString(R.string.control_save)
            binding.negative.text = getString(R.string.control_cancel)
            binding.negative.setOnClickListener { dismiss() }
            binding.neutral.text = getString(R.string.control_delete)
            binding.neutral.setOnClickListener {
                vm.delete(vm.currentSite.value!!)
                dismiss()
            }
        }

        binding.neutral.setOnClickListener {
            vm.copyToClipboard(binding.packPassword.editText?.text.toString())
        }

        binding.positive.setOnClickListener {
            if (binding.packUsername.editText?.text.toString().isEmpty()) {
                binding.packUsername.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            binding.packUsername.error = null
            if (binding.packPassword.editText?.text.toString().isEmpty()) {
                binding.packPassword.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            binding.packPassword.error = null
            vm.saveCredential(
                vm.currentSite.value?.name!!,
                vm.currentSite.value?.url!!,
                binding.packUsername.editText?.text.toString(),
                binding.packPassword.editText?.text.toString()
            )
            dismiss()
        }

        return AlertDialog.Builder(activity!!)
            .setView(binding.root)
            .setCancelable(false)
            .create()
    }
}