package com.tompee.utilities.passwordmanager.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.tompee.utilities.passwordmanager.R
import dagger.android.support.HasSupportFragmentInjector

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity(), HasSupportFragmentInjector {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        val binding: T = DataBindingUtil.setContentView(this, layoutId)
        binding.setLifecycleOwner(this)
        setupBindingAndViewModel(binding)
    }

    protected fun setToolbar(toolbar: Toolbar, homeButtonEnable: Boolean = false) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(homeButtonEnable)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish(Activity.RESULT_OK)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish(Activity.RESULT_OK)
        super.onBackPressed()
    }

    protected open fun finish(result: Int, intent : Intent = Intent()) {
        setResult(result, intent)
        super.finish()
    }

    protected abstract val layoutId: Int

    abstract fun setupBindingAndViewModel(binding: T)

}