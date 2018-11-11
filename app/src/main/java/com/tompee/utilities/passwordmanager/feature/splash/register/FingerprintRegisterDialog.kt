package com.tompee.utilities.passwordmanager.feature.splash.register

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseDialogFragment
import com.tompee.utilities.passwordmanager.feature.splash.SplashViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class FingerprintRegisterDialog : BaseDialogFragment() {

    @Inject
    lateinit var factory : SplashViewModel.Factory

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val vm = ViewModelProviders.of(activity!!, factory)[SplashViewModel::class.java]

        return AlertDialog.Builder(activity!!)
                .setTitle(R.string.title_authenticate)
                .setMessage(R.string.message_fingerprint_not_registered)
                .setPositiveButton(R.string.control_ok) { _, _ ->
                    vm.finish()
                }
                .setCancelable(false)
                .create()
    }

    override fun onResume() {
        super.onResume()
        dialog.setCancelable(false)
    }
}