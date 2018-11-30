package com.tompee.utilities.passwordmanager.interactor

import com.tompee.utilities.passwordmanager.base.BaseInteractor
import com.tompee.utilities.passwordmanager.core.auth.Authenticator
import io.reactivex.Single

class BackupInteractor(private val authenticator: Authenticator) : BaseInteractor {

    fun signup(email: String, pass: String) = authenticator.signup(email, pass)

    fun login(email: String, pass: String): Single<String> = authenticator.login(email, pass)
}