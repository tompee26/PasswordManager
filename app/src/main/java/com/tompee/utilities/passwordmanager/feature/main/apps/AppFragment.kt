package com.tompee.utilities.passwordmanager.feature.main.apps

import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseFragment
import com.tompee.utilities.passwordmanager.databinding.FragmentAppBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class AppFragment : BaseFragment<FragmentAppBinding>() {

    @Inject
    lateinit var factory: AppViewModel.Factory

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun setupBindingAndViewModel(binding: FragmentAppBinding) {
        val vm = ViewModelProviders.of(this, factory)[AppViewModel::class.java]
    }

    override val layoutId: Int = R.layout.fragment_app
}