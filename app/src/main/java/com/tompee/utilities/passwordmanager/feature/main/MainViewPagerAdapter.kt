package com.tompee.utilities.passwordmanager.feature.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.feature.main.apps.AppFragment
import com.tompee.utilities.passwordmanager.feature.main.sites.SitesFragment

class MainViewPagerAdapter(
    fragmentManager: FragmentManager,
    private val context: Context,
    private val appFragment: AppFragment,
    private val sitesFragment: SitesFragment
) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return if (position == 0) appFragment else sitesFragment
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(if (position == 0) R.string.title_applications else R.string.title_sites)
    }
}