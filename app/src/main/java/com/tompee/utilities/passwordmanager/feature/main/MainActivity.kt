package com.tompee.utilities.passwordmanager.feature.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseActivity
import com.tompee.utilities.passwordmanager.databinding.ActivityMainBinding
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding>() {
    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory : MainViewModel.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun setupBindingAndViewModel(binding: ActivityMainBinding) {
        val vm = ViewModelProviders.of(this, factory)[MainViewModel::class.java]
        binding.viewModel = vm
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override val layoutId: Int = R.layout.activity_main
}