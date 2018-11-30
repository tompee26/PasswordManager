package com.tompee.utilities.passwordmanager.feature.auth

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tompee.utilities.passwordmanager.dependency.scope.LoginScope
import com.tompee.utilities.passwordmanager.feature.auth.page.LoginPageFragment
import com.tompee.utilities.passwordmanager.interactor.BackupInteractor
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Named

@Module(includes = [LoginModule.Bindings::class])
class LoginModule {

    @Module
    interface Bindings {
        @ContributesAndroidInjector
        fun bindLoginPageFragment() : LoginPageFragment
    }

    @Provides
    @LoginScope
    fun provideFragmentManager(activity: LoginActivity): FragmentManager = activity.supportFragmentManager

    @Provides
    @Named("login")
    fun provideLoginFragment(): LoginPageFragment = LoginPageFragment.newInstance(LoginPageFragment.LOGIN)

    @Provides
    @Named("signup")
    fun provideSignUpFragment(): LoginPageFragment = LoginPageFragment.newInstance(LoginPageFragment.SIGN_UP)

    @LoginScope
    @Provides
    fun provideLoginPagerAdapter(
        fragmentManager: FragmentManager,
        @Named("login") loginFragment: LoginPageFragment,
        @Named("signup") signupFragment: LoginPageFragment
    ) = LoginPagerAdapter(fragmentManager, loginFragment, signupFragment)

    @LoginScope
    @Provides
    fun provideLoginViewModelFactory(backupInteractor: BackupInteractor,
                                     context: Context) : LoginViewModel.Factory =
        LoginViewModel.Factory(backupInteractor, context)

    @LoginScope
    @Provides
    fun provideBackupInteractor() : BackupInteractor = BackupInteractor()
}