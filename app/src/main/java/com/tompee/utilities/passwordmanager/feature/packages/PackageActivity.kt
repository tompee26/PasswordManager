package com.tompee.utilities.passwordmanager.feature.packages

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseActivity
import com.tompee.utilities.passwordmanager.databinding.ActivityPackageBinding
import com.tompee.utilities.passwordmanager.feature.common.DividerDecorator
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class PackageActivity : BaseActivity<ActivityPackageBinding>() {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory: PackageViewModel.Factory

    @Inject
    lateinit var packageAdapter: PackageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun setupBindingAndViewModel(binding: ActivityPackageBinding) {
        setToolbar(binding.toolbarInclude.toolbar, true)

        binding.list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerDecorator(ContextCompat.getDrawable(context, R.drawable.list_divider)!!))
            adapter = packageAdapter
        }

        val vm = ViewModelProviders.of(this, factory)[PackageViewModel::class.java]
        binding.viewModel = vm

        vm.packageList.observe(this, Observer {
            packageAdapter.packageList = it
        })
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override val layoutId: Int = R.layout.activity_package
}
