package com.tompee.utilities.passwordmanager.feature.license

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseActivity
import com.tompee.utilities.passwordmanager.databinding.ActivityLicenseBinding
import com.tompee.utilities.passwordmanager.feature.splash.SplashActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class LicenseActivity : BaseActivity<ActivityLicenseBinding>() {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory: LicenseViewModel.Factory

    private var isShowAuthentication = false

    companion object {
        private const val REQUEST = 1808
        const val TAG_MODE = "mode"
        const val LICENSE = 1
        const val PRIVACY = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun setupBindingAndViewModel(binding: ActivityLicenseBinding) {
        setToolbar(binding.include.toolbar, true)
        binding.include.toolbarBg.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))

        val vm = ViewModelProviders.of(this, factory)[LicenseViewModel::class.java]
        binding.viewModel = vm

        vm.title.observe(this, Observer {
            binding.include.toolbarTitle.text = it
        })

        vm.message.observe(this, Observer {
            binding.content.movementMethod = LinkMovementMethod()
        })

        vm.onLoaded(intent.getIntExtra(TAG_MODE, LICENSE) == LICENSE)
    }

    override fun finish() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        super.finish()
    }

    override fun onStart() {
        super.onStart()
        if (isShowAuthentication) {
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivityForResult(intent, REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                isShowAuthentication = false
            } else {
                finish()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        isShowAuthentication = true
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override val layoutId: Int
        get() = R.layout.activity_license

}