package com.tompee.utilities.passwordmanager.feature.splash

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseActivity
import com.tompee.utilities.passwordmanager.databinding.ActivitySplashBinding
import com.tompee.utilities.passwordmanager.feature.splash.authenticate.FingerprintDialog
import com.tompee.utilities.passwordmanager.feature.splash.register.FingerprintRegisterDialog
import com.tompee.utilities.passwordmanager.feature.splash.support.FingerprintSupportDialog
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory: SplashViewModel.Factory

    @Inject
    lateinit var fingerprintDialog: FingerprintDialog

    @Inject
    lateinit var fingerprintSupportDialog: FingerprintSupportDialog

    @Inject
    lateinit var fingerprintRegisterDialog: FingerprintRegisterDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun setupBindingAndViewModel(binding: ActivitySplashBinding) {
        val vm = ViewModelProviders.of(this, factory)[SplashViewModel::class.java]
        binding.viewModel = vm

        vm.isFingerprintSupported.observe(this, Observer {
            if (!it) fingerprintSupportDialog.show(supportFragmentManager, "support")
        })


        vm.hasRegisteredFingerprint.observe(this, Observer {
            if (!it) fingerprintRegisterDialog.show(supportFragmentManager, "register")
        })

        vm.needsClose.observe(this, Observer {
            if (it) finish(Activity.RESULT_CANCELED)
        })

        vm.authenticateResult.observe(this, Observer {
            if (it) finish(Activity.RESULT_OK)
        })
    }

    override fun onResume() {
        super.onResume()
        fingerprintDialog.show(supportFragmentManager, "authenticate")
    }

    override fun onPause() {
        super.onPause()
        fingerprintDialog.dismiss()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override val layoutId: Int = R.layout.activity_splash
}