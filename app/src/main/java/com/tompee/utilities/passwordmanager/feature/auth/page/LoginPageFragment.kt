package com.tompee.utilities.passwordmanager.feature.auth.page

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseFragment
import com.tompee.utilities.passwordmanager.databinding.FragmentLoginBinding
import com.tompee.utilities.passwordmanager.feature.auth.LoginViewModel
import com.tompee.utilities.passwordmanager.feature.common.ProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class LoginPageFragment : BaseFragment<FragmentLoginBinding>() {

    private lateinit var progressDialog: ProgressDialog

    @Inject
    lateinit var factory: LoginViewModel.Factory

    companion object {
        const val LOGIN = 0
        const val SIGN_UP = 1
        private const val TYPE_TAG = "type"

        fun newInstance(type: Int): LoginPageFragment {
            val loginFragment = LoginPageFragment()
            val bundle = Bundle()
            bundle.putInt(TYPE_TAG, type)
            loginFragment.arguments = bundle
            return loginFragment
        }
    }

    override fun setupDependencies() {
        AndroidSupportInjection.inject(this)
    }

    override fun setupBindingAndViewModel(binding: FragmentLoginBinding) {
        val vm = ViewModelProviders.of(activity!!, factory)[LoginViewModel::class.java]
        val type = arguments?.getInt(TYPE_TAG) ?: LOGIN
        binding.switchButton.setOnClickListener { vm.pageSwitch(type) }
        if (type == LOGIN) {
            progressDialog = ProgressDialog.newInstance(R.color.colorLoginButton, R.string.progress_login_authenticate)
            binding.switchButton.text = getString(R.string.label_login_new_account)
            binding.commandButton.text = getString(R.string.label_login)
            binding.commandButton.setBackgroundResource(R.drawable.ripple_login)
        } else {
            progressDialog = ProgressDialog.newInstance(R.color.colorSignUpButton, R.string.progress_login_register)
            binding.switchButton.text = getString(R.string.label_login_registered)
            binding.commandButton.text = getString(R.string.label_sign_up)
            binding.commandButton.setBackgroundResource(R.drawable.ripple_sign_up)
        }
    }

    override val layoutId: Int = R.layout.fragment_login
}