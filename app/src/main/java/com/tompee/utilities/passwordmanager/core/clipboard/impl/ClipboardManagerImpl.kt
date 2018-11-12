package com.tompee.utilities.passwordmanager.core.clipboard.impl

import android.content.ClipData
import android.content.Context
import com.tompee.utilities.passwordmanager.core.clipboard.ClipboardManager
import io.reactivex.Completable

class ClipboardManagerImpl(private val context: Context) : ClipboardManager {

    override fun copyToClipboard(text: String): Completable {
        return Completable.fromAction {
            val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("Copied data", text)
            manager.primaryClip = clip
        }
    }
}