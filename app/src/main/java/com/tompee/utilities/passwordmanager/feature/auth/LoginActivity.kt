package com.tompee.utilities.passwordmanager.feature.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseActivity
import com.tompee.utilities.passwordmanager.databinding.ActivityLoginBinding
import com.tompee.utilities.passwordmanager.feature.auth.page.LoginPageFragment
import com.tompee.utilities.passwordmanager.feature.splash.SplashActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class LoginActivity : BaseActivity<ActivityLoginBinding>(), ViewPager.PageTransformer,
    ViewPager.OnPageChangeListener {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var loginPagerAdapter: LoginPagerAdapter

    @Inject
    lateinit var factory: LoginViewModel.Factory

    private lateinit var binding: ActivityLoginBinding

    private var isShowAuthentication = false

    companion object {
        private const val REQUEST = 1810
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun setupBindingAndViewModel(binding: ActivityLoginBinding) {
        this.binding = binding
        val vm = ViewModelProviders.of(this, factory)[LoginViewModel::class.java]

        vm.switchPage.observe(this, Observer {
            if (it == LoginPageFragment.LOGIN) {
                binding.viewpager.currentItem = 1
            } else {
                binding.viewpager.currentItem = 0
            }
        })

        binding.viewpager.apply {
            addOnPageChangeListener(this@LoginActivity)
            setPageTransformer(false, this@LoginActivity)
            adapter = loginPagerAdapter
        }
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

    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        when {
            position < -1 -> view.alpha = 0f
            position <= 1 -> {
                view.findViewById<View>(R.id.tv_app_name).translationX = -(pageWidth * position)
                view.findViewById<View>(R.id.tv_app_subtitle).translationX = -(pageWidth * position)

                view.findViewById<View>(R.id.userView).translationX = pageWidth * position
                view.findViewById<View>(R.id.tv_user_label).translationX = pageWidth * position
                view.findViewById<View>(R.id.view_user_underline).translationX = pageWidth * position
                view.findViewById<View>(R.id.profileImage).translationX = pageWidth * position

                view.findViewById<View>(R.id.passView).translationX = pageWidth * position
                view.findViewById<View>(R.id.tv_pass_label).translationX = pageWidth * position
                view.findViewById<View>(R.id.view_pass_underline).translationX = pageWidth * position
                view.findViewById<View>(R.id.iv_pass_icon).translationX = pageWidth * position

                view.findViewById<View>(R.id.commandButton).translationX = -(pageWidth * position)
            }
            else -> view.alpha = 0f
        }
    }

    private fun computeFactor(): Float {
        return (binding.imageView.width / 2 - binding.viewpager.width) / (binding.viewpager.width *
                loginPagerAdapter.count - 1).toFloat()
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        val x = ((binding.viewpager.width * position + positionOffsetPixels) * computeFactor())
        binding.scrollView.scrollTo(x.toInt() + binding.imageView.width / 3, 0)
    }

    override fun onPageSelected(position: Int) {
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override val layoutId: Int = R.layout.activity_login
}