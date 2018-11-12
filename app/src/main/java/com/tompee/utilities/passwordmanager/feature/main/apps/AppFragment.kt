package com.tompee.utilities.passwordmanager.feature.main.apps

import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseFragment
import com.tompee.utilities.passwordmanager.databinding.FragmentAppBinding
import com.tompee.utilities.passwordmanager.feature.common.DividerDecorator
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class AppFragment : BaseFragment<FragmentAppBinding>() {

    @Inject
    lateinit var factory: AppViewModel.Factory

    @Inject
    lateinit var appAdapter: AppAdapter

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun setupBindingAndViewModel(binding: FragmentAppBinding) {
        val vm = ViewModelProviders.of(this, factory)[AppViewModel::class.java]
        binding.list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerDecorator(ContextCompat.getDrawable(context, R.drawable.list_divider)!!))
            adapter = appAdapter
        }

        vm.list.observe(this, Observer {
            appAdapter.addPackages(it)
        })
    }

    override val layoutId: Int = R.layout.fragment_app
}