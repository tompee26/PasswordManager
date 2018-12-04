package com.tompee.utilities.passwordmanager.feature.backup.clean

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseDialogFragment
import com.tompee.utilities.passwordmanager.databinding.DialogCleanBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class CleanDialog : BaseDialogFragment() {

    @Inject
    lateinit var factory: CleanDialogViewModel.Factory

    lateinit var binding: DialogCleanBinding

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val vm = ViewModelProviders.of(this, factory)[CleanDialogViewModel::class.java]
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_clean,
            null,
            false
        )
        vm.cleanError.observe(this, Observer {
            if (it.isNotEmpty()) {
                binding.pass1.error = it
            } else {
                binding.pass1.error = null
            }
        })
        vm.cleanOngoing.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.textView.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.textView.visibility = View.GONE
            }
            binding.pass1.isEnabled = !it
        })
        vm.cleanFinished.observe(this, Observer {
            if (it) dismiss()
        })
        return AlertDialog.Builder(activity!!)
            .setTitle(R.string.title_delete)
            .setView(binding.root)
            .setCancelable(false)
            .setPositiveButton(R.string.control_proceed) { _, _ -> }
            .setNegativeButton(R.string.control_cancel) { _, _ -> dismiss() }
            .create()
    }

    override fun onResume() {
        super.onResume()
        val d = dialog as AlertDialog
        val positiveButton = d.getButton(Dialog.BUTTON_POSITIVE) as Button
        positiveButton.setOnClickListener {
            val vm = ViewModelProviders.of(this, factory)[CleanDialogViewModel::class.java]
            if (binding.pass1.editText?.text.toString().isEmpty()) {
                binding.pass1.error = getString(R.string.error_empty)
                return@setOnClickListener
            }
            binding.pass1.error = null
            hideKeyboard()
            vm.clean(binding.pass1.editText?.text.toString())
        }
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}