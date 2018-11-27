package com.tompee.utilities.passwordmanager.feature.packages

import android.app.Activity
import android.content.Intent
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
import com.tompee.utilities.passwordmanager.feature.packages.add.AddPackageDialog
import com.tompee.utilities.passwordmanager.feature.splash.SplashActivity
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

    private var isShowAuthentication = false

    companion object {
        private const val REQUEST = 1808
    }

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

        packageAdapter.listener = {
            vm.setCurrentPackage(it)
            val dialog = AddPackageDialog()
            dialog.show(supportFragmentManager, "package")
        }

        vm.packageList.observe(this, Observer {
            packageAdapter.packageList = it
        })
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
                finish(Activity.RESULT_CANCELED)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        isShowAuthentication = true
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override val layoutId: Int = R.layout.activity_package
}
