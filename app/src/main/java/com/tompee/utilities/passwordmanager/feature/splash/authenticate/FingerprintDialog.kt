package com.tompee.utilities.passwordmanager.feature.splash.authenticate

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseDialogFragment
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
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_authenticate, null)!!
        val icon = view.findViewById<ImageView>(R.id.icFingerprint)

        vm.startAuthentication()
        vm.authenticateResult.observe(this, Observer {
            if (it) dismiss() else {
                YoYo.with(Techniques.Shake)
                    .duration(ANIMATION_DURATION)
                    .playOn(icon)
            }
        })

        val builder = AlertDialog.Builder(activity!!)
            .setView(view)
            .setNegativeButton(R.string.control_cancel) { _, _ ->
                vm.finish()
            }
            .setCancelable(false)
        return builder.create()
    }
}