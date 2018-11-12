package com.tompee.utilities.passwordmanager.feature.main.sites

import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseFragment
import com.tompee.utilities.passwordmanager.databinding.FragmentSitesBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SitesFragment : BaseFragment<FragmentSitesBinding>() {

    @Inject
    lateinit var factory: SitesViewModel.Factory

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun setupBindingAndViewModel(binding: FragmentSitesBinding) {
        val vm = ViewModelProviders.of(activity!!, factory)[SitesViewModel::class.java]
    }

    override val layoutId: Int = R.layout.fragment_sites
}