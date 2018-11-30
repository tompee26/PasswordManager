package com.tompee.utilities.passwordmanager.feature.splash.authenticate

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseDialogFragment
import com.tompee.utilities.passwordmanager.databinding.DialogAuthenticateBinding
import com.tompee.utilities.passwordmanager.feature.splash.SplashViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class FingerprintDialog : BaseDialogFragment() {

    @Inject
    lateinit var factory: SplashViewModel.Factory

    companion object {
        private const val ANIMATION_DURATION = 700L
    }

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val vm = ViewModelProviders.of(activity!!, factory)[SplashViewModel::class.java]
        val binding = DataBindingUtil.inflate<DialogAuthenticateBinding>(
            LayoutInflater.from(context), R.layout.dialog_authenticate, null, false)

        vm.startAuthentication()
        vm.authenticateResult.observe(this, Observer {
            if (it) dismiss() else {
                YoYo.with(Techniques.Shake)
                    .duration(ANIMATION_DURATION)
                    .playOn(binding.icFingerprint)
            }
        })

        val builder = AlertDialog.Builder(activity!!)
            .setView(binding.root)
            .setNegativeButton(R.string.control_cancel) { _, _ ->
                vm.requestFinish(false)
            }
            .setCancelable(false)
        return builder.create()
    }
}