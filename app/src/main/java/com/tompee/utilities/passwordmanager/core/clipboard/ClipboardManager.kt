package com.tompee.utilities.passwordmanager.core.clipboard

import io.reactivex.Completable

interface ClipboardManager {
    fun copyToClipboard(text: String): Completable
}