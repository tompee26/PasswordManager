package com.tompee.utilities.passwordmanager.service

import android.os.CancellationSignal
import android.service.autofill.*
import android.view.autofill.AutofillValue
import android.widget.RemoteViews
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.service.model.AuthField
import com.tompee.utilities.passwordmanager.service.parser.StructureParser
import com.tompee.utilities.passwordmanager.service.response.ResponseBuilder
import dagger.android.AndroidInjection
import javax.inject.Inject

class FillService : AutofillService() {

    @Inject
    lateinit var parser: StructureParser

    @Inject
    lateinit var responseBuilder: ResponseBuilder

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onFillRequest(request: FillRequest, cancellationSignal: CancellationSignal, callback: FillCallback) {
        val structure = request.fillContexts[request.fillContexts.size - 1].structure
        val appPackageName = structure.activityComponent.packageName

        val fields = parser.parse(structure)
        callback.onSuccess(responseBuilder.createResponse(appPackageName, fields))
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
    }
}