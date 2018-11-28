package com.tompee.utilities.passwordmanager.service.response

import android.content.Context
import android.service.autofill.Dataset
import android.service.autofill.FillResponse
import android.service.autofill.SaveInfo
import android.view.autofill.AutofillValue
import android.widget.RemoteViews
import androidx.annotation.NonNull
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

    fun createResponse(
        appPackageName: String,
        authField: AuthField
    ): FillResponse? {
        if (authField.usernameField == null && authField.passwordField == null) return null

        val data = dataset.find { it.packageName == appPackageName } ?: return null

        val datasetBuilder = Dataset.Builder()
        if (authField.usernameField != null) {
            datasetBuilder.setValue(
                authField.usernameField?.autofillId!!,
                AutofillValue.forText(data.username),
                newDatasetPresentation(context.packageName, "my_username")
            )
        }
        if (authField.passwordField != null) {
            datasetBuilder.setValue(
                authField.passwordField?.autofillId!!,
                AutofillValue.forText(data.password),
                newDatasetPresentation(context.packageName, "Password for my_username")
            )
        }
        return FillResponse.Builder()
            .addDataset(datasetBuilder.build())
            .build()
    }

    private fun newDatasetPresentation(
        @NonNull packageName: String,
        @NonNull text: CharSequence
    ): RemoteViews {
        val presentation = RemoteViews(packageName, R.layout.list_dataset)
        presentation.setTextViewText(R.id.text, text)
        presentation.setImageViewResource(R.id.icon, R.mipmap.ic_launcher)
        return presentation
    }

    fun stop() {
        subscription.dispose()
    }
}