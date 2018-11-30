package com.tompee.utilities.passwordmanager.feature.about

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseAuthActivity
import com.tompee.utilities.passwordmanager.databinding.ActivityAboutBinding
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class AboutActivity : BaseAuthActivity<ActivityAboutBinding>() {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory: AboutViewModel.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun setupBindingAndViewModel(binding: ActivityAboutBinding) {
        setToolbar(binding.include.toolbar, true)
        binding.include.toolbarBg.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))

        val vm = ViewModelProviders.of(this, factory)[AboutViewModel::class.java]
        binding.viewModel = vm

        vm.title.observe(this, Observer {
            binding.include.toolbarTitle.text = it
        })
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override val layoutId: Int = R.layout.activity_about

    override fun isAuthInitiallyRequired(): Boolean = false
}