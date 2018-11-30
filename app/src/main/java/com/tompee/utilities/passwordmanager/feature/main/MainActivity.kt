package com.tompee.utilities.passwordmanager.feature.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.tompee.utilities.passwordmanager.Constants
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseAuthActivity
import com.tompee.utilities.passwordmanager.databinding.ActivityMainBinding
import com.tompee.utilities.passwordmanager.feature.about.AboutActivity
import com.tompee.utilities.passwordmanager.feature.license.LicenseActivity
import com.tompee.utilities.passwordmanager.feature.main.addsites.AddSitesDialog
import com.tompee.utilities.passwordmanager.feature.main.backup.BackupDialog
import com.tompee.utilities.passwordmanager.feature.packages.PackageActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class MainActivity : BaseAuthActivity<ActivityMainBinding>() {
    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var mainPagerAdapter: MainViewPagerAdapter

    @Inject
    lateinit var factory: MainViewModel.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent: Intent
        when (item.itemId) {
            R.id.menu_backup -> {
                val dialog = BackupDialog()
                dialog.show(supportFragmentManager, "backup")
                return true
            }
            R.id.menu_about -> {
                intent = Intent(this, AboutActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivityForResult(intent, Constants.REQUEST_CODE)
                return true
            }
            R.id.menu_policy -> {
                intent = Intent(this, LicenseActivity::class.java)
                intent.putExtra(LicenseActivity.TAG_MODE, LicenseActivity.PRIVACY)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivityForResult(intent, Constants.REQUEST_CODE)
                return true
            }
            R.id.menu_license -> {
                intent = Intent(this, LicenseActivity::class.java)
                intent.putExtra(LicenseActivity.TAG_MODE, LicenseActivity.LICENSE)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivityForResult(intent, Constants.REQUEST_CODE)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setupBindingAndViewModel(binding: ActivityMainBinding) {
        setToolbar(binding.toolbarInclude.toolbar, false)
        binding.viewPager.apply {
            offscreenPageLimit = mainPagerAdapter.count
            adapter = mainPagerAdapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                    when (state) {
                        ViewPager.SCROLL_STATE_DRAGGING -> binding.add.hide()
                        ViewPager.SCROLL_STATE_SETTLING, ViewPager.SCROLL_STATE_IDLE -> binding.add.show()
                    }
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                }
            })
        }

        binding.add.setOnClickListener {
            if (binding.viewPager.currentItem == 0) {
                val intent = Intent(this@MainActivity, PackageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivityForResult(intent, Constants.REQUEST_CODE)
            } else {
                val dialog = AddSitesDialog()
                dialog.show(supportFragmentManager, "addsites")
            }
        }

        val vm = ViewModelProviders.of(this, factory)[MainViewModel::class.java]
        binding.viewModel = vm
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override val layoutId: Int = R.layout.activity_main

    override fun isAuthInitiallyRequired(): Boolean = true
}