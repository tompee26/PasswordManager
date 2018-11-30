package com.tompee.utilities.passwordmanager.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.tompee.utilities.passwordmanager.Constants
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.feature.splash.SplashActivity
import dagger.android.support.HasSupportFragmentInjector

abstract class BaseAuthActivity<T : ViewDataBinding> : AppCompatActivity(), HasSupportFragmentInjector {

    private var isShowAuthentication = isAuthInitiallyRequired()

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish(Activity.RESULT_OK)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        finish(Activity.RESULT_OK)
        super.onBackPressed()
    }

    protected open fun finish(result: Int, intent: Intent = Intent()) {
        setResult(result, intent)
        super.finish()
    }

    protected abstract val layoutId: Int

    abstract fun isAuthInitiallyRequired(): Boolean

    abstract fun setupBindingAndViewModel(binding: T)

    override fun onStart() {
        super.onStart()
        if (isShowAuthentication) {
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivityForResult(intent, Constants.REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE) {
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
}