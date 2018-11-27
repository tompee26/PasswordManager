package com.tompee.utilities.passwordmanager.service

import android.os.CancellationSignal
import android.service.autofill.*

class FillService : AutofillService() {
    override fun onFillRequest(request: FillRequest, cancellationSignal: CancellationSignal, callback: FillCallback) {

    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
    }
}