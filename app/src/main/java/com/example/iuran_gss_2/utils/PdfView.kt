import android.os.AsyncTask
import com.github.barteksc.pdfviewer.PDFView
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RetrievePDFfromUrl(private val pdfView: PDFView) : AsyncTask<String?, Void?, InputStream?>() {

    override fun doInBackground(vararg strings: String?): InputStream? {
        var inputStream: InputStream? = null
        try {
            val url = URL(strings[0])
            val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection
            if (urlConnection.responseCode == 200) {
                inputStream = BufferedInputStream(urlConnection.inputStream)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return inputStream
    }

    override fun onPostExecute(inputStream: InputStream?) {
        pdfView.fromStream(inputStream).load()
    }
}