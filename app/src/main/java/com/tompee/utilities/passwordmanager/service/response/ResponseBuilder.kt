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
import com.tompee.utilities.passwordmanager.core.keystore.Keystore
import com.tompee.utilities.passwordmanager.core.packages.PackageManager
import com.tompee.utilities.passwordmanager.model.PackageCredential
import com.tompee.utilities.passwordmanager.service.model.AuthField
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ResponseBuilder(
    private val context: Context,
    private val packageDao: PackageDao,
    private val packageManager: PackageManager,
    private val keystore: Keystore,
    private val cipher: Cipher
) {

    private lateinit var subscription: Disposable
    private var dataset: List<PackageCredential> = emptyList()

    fun start() {
        subscription = packageDao.getPackages()
            .concatMap { list ->
                Observable.fromIterable(list)
                    .concatMapSingle { entity ->
                        packageManager.getPackageFromName(entity.packageName)
                            .map {
                                val key = keystore.getKey(it.packageName)!!
                                return@map PackageCredential(
                                    it.name,
                                    it.packageName,
                                    cipher.decrypt(entity.username, key.private),
                                    cipher.decrypt(entity.password, key.private),
                                    it.icon
                                )
                            }
                    }
                    .toList()
                    .toObservable()
            }
            .subscribeOn(Schedulers.io())
            .subscribe { dataset = it }
    }

    fun createResponse(appPackageName: String, authField: AuthField): FillResponse? {
        if (authField.usernameField == null && authField.passwordField == null) return null

        try {
            val data = packageDao.findPackage(appPackageName)
                .map {
                    val key = keystore.getKey(appPackageName)!!
                    return@map PackageCredential(
                        it.name,
                        it.packageName,
                        cipher.decrypt(it.username, key.private),
                        cipher.decrypt(it.password, key.private),
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
                    newDatasetPresentation(R.drawable.ic_account_circle_black_24dp)
                )
            }
            if (authField.passwordField != null) {
                datasetBuilder.setValue(
                    authField.passwordField?.autofillId!!,
                    AutofillValue.forText(data.password),
                    newDatasetPresentation(R.drawable.ic_account_circle_black_24dp)
                )
            }
            return FillResponse.Builder()
                .addDataset(datasetBuilder.build())
                .build()

        } catch (e: Exception) {
            return null
        }
    }

    private fun newDatasetPresentation(@DrawableRes id: Int): RemoteViews {
        val presentation = RemoteViews(context.packageName, R.layout.list_dataset)
        presentation.setTextViewText(R.id.text, context.getString(R.string.app_name))
        presentation.setImageViewResource(R.id.icon, id)
        return presentation
    }
}