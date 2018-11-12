package com.tompee.utilities.passwordmanager.core.asset

import android.content.Context
import android.graphics.drawable.Drawable
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


class AssetManager(private val context: Context) {

    fun getDrawableFromAsset(filename: String): Drawable? {
        return try {
            val ims = context.assets.open(filename)
            Drawable.createFromStream(ims, null)
        } catch (ex: IOException) {
            null
        }
    }

    fun getStringFromAsset(filename: String): String {
        val buffer = StringBuilder()
        val inputStream: InputStream
        try {
            inputStream = context.assets.open(filename)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var str: String? = bufferedReader.readLine()
            while (str != null) {
                buffer.append(str)
                str = bufferedReader.readLine()
            }
            bufferedReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return buffer.toString()
    }
}