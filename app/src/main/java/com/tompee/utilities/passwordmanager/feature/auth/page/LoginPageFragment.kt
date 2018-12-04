package com.tompee.utilities.passwordmanager.feature.auth.page

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
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
        binding.viewModel = vm

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

        vm.emailError.observe(this, Observer {
            when (it) {
                LoginViewModel.InputError.EMAIL_EMPTY -> {
                    binding.userView.error =
                            getString(R.string.error_field_required)
                }
                LoginViewModel.InputError.EMAIL_INVALID -> {
                    binding.userView.error =
                            getString(R.string.error_invalid_email)
                }
                else -> binding.userView.error = null
            }
        })

        vm.passwordError.observe(this, Observer {
            when (it) {
                LoginViewModel.InputError.PASSWORD_EMPTY -> {
                    binding.passView.error =
                            getString(R.string.error_field_required)
                }
                LoginViewModel.InputError.PASSWORD_SHORT -> {
                    binding.passView.error =
                            getString(R.string.error_pass_min)
                }
                else -> binding.passView.error = null
            }
        })

        vm.processing.observe(this, Observer {
            if (userVisibleHint) {
                if (it) {
                    progressDialog.show(fragmentManager!!, "progress")
                } else {
                    progressDialog.dismiss()
                }
            }
        })

        vm.commandError.observe(this, Observer {
            if (userVisibleHint) {
                Snackbar.make(
                    activity?.findViewById(android.R.id.content)!!,
                    it, Snackbar.LENGTH_LONG
                ).show()
            }
        })

        binding.commandButton.setOnClickListener {
            hideKeyboard()
            if (type == LOGIN) {
                vm.startLogin()
            } else {
                vm.startSignup()
            }
        }
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override val layoutId: Int = R.layout.fragment_login
}