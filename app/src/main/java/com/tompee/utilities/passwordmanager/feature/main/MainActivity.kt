package com.tompee.utilities.passwordmanager.feature.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseActivity
import com.tompee.utilities.passwordmanager.databinding.ActivityMainBinding
import com.tompee.utilities.passwordmanager.feature.main.addsites.AddSitesDialog
import com.tompee.utilities.passwordmanager.feature.packages.PackageActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding>() {
    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var mainPagerAdapter : MainViewPagerAdapter

    @Inject
    lateinit var factory : MainViewModel.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun setupBindingAndViewModel(binding: ActivityMainBinding) {
        binding.viewPager.apply {
            offscreenPageLimit = mainPagerAdapter.count
            adapter = mainPagerAdapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                    when(state) {
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
                startActivity(intent)
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
}