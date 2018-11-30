package com.tompee.utilities.passwordmanager.feature.splash

import androidx.fragment.app.FragmentManager
import com.tompee.utilities.passwordmanager.feature.splash.activate.ActivateDialog
import com.tompee.utilities.passwordmanager.feature.splash.authenticate.FingerprintDialog
import com.tompee.utilities.passwordmanager.feature.splash.register.FingerprintRegisterDialog
import com.tompee.utilities.passwordmanager.feature.splash.support.FingerprintSupportDialog

class SplashDialogManager(
    private val fragmentManager: FragmentManager,
    private val fingerprintSupportDialog: FingerprintSupportDialog,
    private val fingerprintRegisterDialog: FingerprintRegisterDialog,
    private val activateDialog: ActivateDialog,
    private val fingerprintDialog: FingerprintDialog
) {
    enum class Dialogs {
        FINGERPRINT_NOT_SUPPORTED,
        NO_REGISTERED_FINGERPRINTS,
        ENABLE_AUTOFILL,
        AUTHENTICATION
    }

    fun showDialog(dialog: Dialogs) {
        when (dialog) {
            Dialogs.FINGERPRINT_NOT_SUPPORTED -> fingerprintSupportDialog.show(
                fragmentManager, Dialogs.FINGERPRINT_NOT_SUPPORTED.toString()
            )
            Dialogs.NO_REGISTERED_FINGERPRINTS -> fingerprintRegisterDialog.show(
                fragmentManager, Dialogs.NO_REGISTERED_FINGERPRINTS.toString()
            )
            Dialogs.ENABLE_AUTOFILL -> activateDialog.show(
                fragmentManager, Dialogs.ENABLE_AUTOFILL.toString()
            )
            Dialogs.AUTHENTICATION -> fingerprintDialog.show(fragmentManager, Dialogs.AUTHENTICATION.toString())
        }
    }
}