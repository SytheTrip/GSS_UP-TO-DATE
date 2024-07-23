package com.example.iuran_gss_2.utils

import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AlertDialog
import java.io.File
import java.util.concurrent.ExecutionException

class PDFTools {
    private val TAG = "PDFTools"
    private val GOOGLE_DRIVE_PDF_READER_PREFIX = "http://drive.google.com/viewer?url="
    private val PDF_MIME_TYPE = "application/pdf"
    private val HTML_MIME_TYPE = "text/html"


    fun showPDFUrl(context: Context?, pdfUrl: String?) {
        if (isPDFSupported(context!!)) {
            downloadAndOpenPDF(context, pdfUrl!!)
        } else {
            askToOpenPDFThroughGoogleDrive(context, pdfUrl!!)
        }
    }
    fun downloadAndOpenPDF(context: Context, pdfUrl: String) {
        var filename = ""
        try {
            filename = GetFileInfo().execute(pdfUrl).get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

        val tempFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), filename)
        Log.e(TAG, "File Path: $tempFile")
        if (tempFile.exists()) {
            openPDF(context, Uri.fromFile(tempFile))
            return
        }

        val progress = ProgressDialog.show(
            context,
            "PDF",
            "Tunggu woy",
            true
        )

        val request = DownloadManager.Request(Uri.parse(pdfUrl))
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, filename)
        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (!progress.isShowing) return
                context.unregisterReceiver(this)

                progress.dismiss()
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                val cursor: Cursor = dm.query(DownloadManager.Query().setFilterById(downloadId))

                if (cursor.moveToFirst()) {
                    val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    if (statusIndex != -1) {
                        val status = cursor.getInt(statusIndex)
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            openPDF(context, Uri.fromFile(tempFile))
                        }
                    }
                }
                cursor.close()
            }
        }
        context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            Context.RECEIVER_NOT_EXPORTED)

        // Enqueue the request
        dm.enqueue(request)
    }

    fun askToOpenPDFThroughGoogleDrive(context: Context, pdfUrl: String) {
        AlertDialog.Builder(context)
            .setTitle("Download PDF")
            .setMessage("Ingin Download ?")
            .setNegativeButton("Tidak", null)
            .setPositiveButton("Ya") { _, _ ->
                openPDFThroughGoogleDrive(context, pdfUrl)
            }
            .show()
    }

    fun openPDFThroughGoogleDrive(context: Context, pdfUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(Companion.GOOGLE_DRIVE_PDF_READER_PREFIX + pdfUrl), HTML_MIME_TYPE)
        context.startActivity(intent)
    }

    fun openPDF(context: Context, localUri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(localUri, PDF_MIME_TYPE)
        context.startActivity(intent)
    }

    private fun isPDFSupported(context: Context): Boolean {
        val intent = Intent(Intent.ACTION_VIEW)
        val mimeTypes = arrayOf(PDF_MIME_TYPE)
        intent.setType(PDF_MIME_TYPE)
        val resolvedActivities = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolvedActivities.isNotEmpty()
    }

    private class GetFileInfo : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg urls: String): String {
            // Implement the logic to get file information, e.g., file name from the URL
            return urls[0].substring(urls[0].lastIndexOf("/") + 1)
        }
    }

    companion object {
        private const val GOOGLE_DRIVE_PDF_READER_PREFIX = "http://drive.google.com/viewer?url="
    }
}
