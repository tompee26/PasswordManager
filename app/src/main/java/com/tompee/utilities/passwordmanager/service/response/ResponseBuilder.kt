package com.tompee.utilities.passwordmanager.service.response

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.service.autofill.Dataset
import android.service.autofill.FillResponse
import android.view.autofill.AutofillValue
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.database.PackageDao
import com.tompee.utilities.passwordmanager.feature.splash.SplashActivity
import com.tompee.utilities.passwordmanager.model.PackageCredential
import com.tompee.utilities.passwordmanager.service.model.AuthField
import io.reactivex.schedulers.Schedulers

class ResponseBuilder(
    private val packageDao: PackageDao,
    private val cipher: Cipher
) {

    fun createResponse(serviceContext: Context, appPackageName: String, authField: AuthField): FillResponse? {
        if (authField.usernameField == null && authField.passwordField == null) return null

        try {
            val data = packageDao.findPackage(appPackageName)
                .map {
                    PackageCredential(
                        it.name,
                        it.packageName,
                        cipher.decrypt(it.username, appPackageName),
                        cipher.decrypt(it.password, appPackageName),
                        ShapeDrawable()
                    )
                }
                .subscribeOn(Schedulers.io())
                .blockingGet()
            val datasetBuilder = Dataset.Builder()
            if (authField.usernameField != null) {
                datasetBuilder.setValue(
                    authField.usernameField?.autofillId!!,
                    AutofillValue.forText(data.username),
                    newDatasetPresentation(serviceContext, R.drawable.ic_account_circle_black_24dp)
                )
            }
            if (authField.passwordField != null) {
                datasetBuilder.setValue(
                    authField.passwordField?.autofillId!!,
                    AutofillValue.forText(data.password),
                    newDatasetPresentation(serviceContext, R.drawable.ic_account_circle_black_24dp)
                )
            }
            val unlockedDataset = datasetBuilder.build()
            val lockedDatasetBuilder = Dataset.Builder()
            val authentication = SplashActivity.newIntentSender(serviceContext, unlockedDataset)
            if (authField.usernameField != null) {
                val presentation = newDatasetPresentation(serviceContext, R.drawable.ic_account_circle_black_24dp)
                lockedDatasetBuilder.setValue(authField.usernameField?.autofillId!!, null, presentation)
                    .setAuthentication(authentication)
            }
            if (authField.passwordField != null) {
                val presentation = newDatasetPresentation(serviceContext, R.drawable.ic_account_circle_black_24dp)
                lockedDatasetBuilder.setValue(authField.passwordField?.autofillId!!, null, presentation)
                    .setAuthentication(authentication)
            }

            return FillResponse.Builder()
                .addDataset(lockedDatasetBuilder.build())
                .build()

        } catch (e: Exception) {
            return null
        }
    }

    private fun newDatasetPresentation(context: Context, @DrawableRes id: Int): RemoteViews {
        val presentation = RemoteViews(context.packageName, R.layout.list_dataset)
        presentation.setTextViewText(R.id.text, context.getString(R.string.app_name))
        presentation.setImageViewResource(R.id.icon, id)
        return presentation
    }
}