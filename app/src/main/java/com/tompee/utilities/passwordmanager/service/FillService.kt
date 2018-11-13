package com.tompee.utilities.passwordmanager.service

import android.os.CancellationSignal
import android.service.autofill.*

class FillService : AutofillService() {
    override fun onFillRequest(request: FillRequest, cancellationSignal: CancellationSignal, callback: FillCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}