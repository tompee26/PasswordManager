package com.tompee.utilities.passwordmanager.core.autofill.impl

import android.content.Context
import com.tompee.utilities.passwordmanager.core.autofill.AutofillManager
import io.reactivex.Single

class AutofillManagerImpl(private val context: Context) : AutofillManager {

    override fun isEnabled() : Single<Boolean> {
        return Single.fromCallable{
            val manager = context.getSystemService(android.view.autofill.AutofillManager::class.java)
            return@fromCallable manager.hasEnabledAutofillServices()
        }
    }
}