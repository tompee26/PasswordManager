package com.tompee.utilities.passwordmanager.core.clipboard

import android.content.ClipData
import android.content.Context
import io.reactivex.Completable

class ClipboardManager(private val context: Context) {

    fun copyToClipboard(text: String): Completable {
        return Completable.fromAction {
            val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("Copied data", text)
            manager.primaryClip = clip
        }
    }
}