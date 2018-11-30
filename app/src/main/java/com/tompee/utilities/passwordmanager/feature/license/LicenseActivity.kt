package com.tompee.utilities.passwordmanager.feature.license

import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseAuthActivity
import com.tompee.utilities.passwordmanager.databinding.ActivityLicenseBinding
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class LicenseActivity : BaseAuthActivity<ActivityLicenseBinding>() {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory: LicenseViewModel.Factory

    companion object {
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

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override val layoutId: Int = R.layout.activity_license

    override fun isAuthInitiallyRequired(): Boolean = false
}