package com.example.iuran_gss_2.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun Uri.getRealPathFromUri(activity: Activity, context: Context): String? {
    var realPath: String? = null
    val contentResolver: ContentResolver = activity.contentResolver
    val projection = arrayOf(MediaStore.Images.Media.DATA)

    // Versi Android sebelum Android Q (10)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        val cursor = contentResolver.query(this, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (it.moveToFirst()) {
                realPath = it.getString(columnIndex)
            }
        }
    } else { // Versi Android Q (10) dan di atasnya
        val openable =
            this.toString().startsWith(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString())
        if (openable) {
            val cursor = contentResolver.query(this, projection, null, null, null)
            cursor?.use {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (it.moveToFirst()) {
                    realPath = it.getString(columnIndex)
                }
            }
        } else {
            val cursor = contentResolver.query(this, null, null, null, null)
            cursor?.use {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                it.moveToFirst()
                val fileName = it.getString(nameIndex)
                val cacheDir = context.externalCacheDir
                val tempFile = File(cacheDir, fileName)
                try {
                    contentResolver.openInputStream(this)?.use { inputStream ->
                        FileOutputStream(tempFile).use { outputStream ->
                            val buffer = ByteArray(4 * 1024)
                            var bytesRead: Int
                            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                                outputStream.write(buffer, 0, bytesRead)
                            }
                        }
                    }
                    realPath = tempFile.absolutePath
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
    return realPath
}

