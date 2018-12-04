package com.tompee.utilities.passwordmanager.interactor

import com.tompee.utilities.passwordmanager.base.BaseInteractor
import com.tompee.utilities.passwordmanager.core.auth.Authenticator
import com.tompee.utilities.passwordmanager.model.UserContainer
import io.reactivex.Single

class LoginInteractor(
    private val authenticator: Authenticator,
    private val userContainer: UserContainer
) : BaseInteractor {

    fun signup(email: String, pass: String) = authenticator.signup(email, pass)
        .doOnSuccess { userContainer.userId = it }

    fun login(email: String, pass: String): Single<String> = authenticator.login(email, pass)
        .doOnSuccess { userContainer.userId = it }
}