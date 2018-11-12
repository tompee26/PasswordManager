package com.tompee.utilities.passwordmanager.feature.main.sites

import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseFragment
import com.tompee.utilities.passwordmanager.databinding.FragmentSitesBinding
import com.tompee.utilities.passwordmanager.feature.common.DividerDecorator
import com.tompee.utilities.passwordmanager.feature.main.common.SitesViewModel
import com.tompee.utilities.passwordmanager.feature.main.view.ViewSitesDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SitesFragment : BaseFragment<FragmentSitesBinding>() {

    @Inject
    lateinit var factory: SitesViewModel.Factory

    @Inject
    lateinit var sitesAdapter: SitesAdapter

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun setupBindingAndViewModel(binding: FragmentSitesBinding) {
        val vm = ViewModelProviders.of(activity!!, factory)[SitesViewModel::class.java]
        binding.list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerDecorator(ContextCompat.getDrawable(context, R.drawable.list_divider)!!))
            adapter = sitesAdapter
        }

        sitesAdapter.listener = {
            vm.setCurrentSite(it)
            val dialog = ViewSitesDialog()
            dialog.show(fragmentManager, "view")
        }

        vm.copiedToClipboard.observe(this, Observer {
            if (it) Toast.makeText(context, getString(R.string.message_clipboard), Toast.LENGTH_SHORT).show()
        })

        vm.list.observe(this, Observer {
            sitesAdapter.addSites(it)
        })
    }

    override val layoutId: Int = R.layout.fragment_sites
}