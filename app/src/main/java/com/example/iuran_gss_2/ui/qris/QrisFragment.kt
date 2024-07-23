package com.example.iuran_gss_2.ui.qris

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.databinding.FragmentQrisBinding
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.utils.formatPrice
import com.example.iuran_gss_2.utils.getPrice
import com.example.iuran_gss_2.utils.getRealPathFromUri
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.random.Random


@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
class QrisFragment : Fragment() {
    private lateinit var binding: FragmentQrisBinding
    private val viewModel: QrisViewModel by viewModel()
    private lateinit var kode: String
    private lateinit var imageView: ImageView
    private lateinit var image: Bitmap
    private val photoParts = mutableListOf<MultipartBody.Part>()
    private val data = JSONObject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQrisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kode = arguments?.getString("title").toString()
        setupPrice()

        navigate()
        setData()
    }

    private fun setData() {
        binding.apply {
            tvPaymentNumberValue.text = generateRandomCode()
        }
    }

    private fun setupPrice() {
        binding.apply {
            val price = getPrice(kode)
            tvNominalValue.text = formatPrice(price)
        }
    }

    private fun generateRandomCode(): String {
        val randomNumber = Random.nextInt(1000, 10000)
        return kode + randomNumber
    }

    private fun navigate() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnClose.setOnClickListener {
            showMainView()
        }
        binding.btnOpenImage.setOnClickListener {
            showImageSourceDialog()
        }
        binding.btnSend.setOnClickListener {
            uploadTransaction()
            Snackbar.make(requireView(), "Sedang mengirim", Toast.LENGTH_SHORT).show()
        }
    }


    private fun uploadTransaction() {
        data.put(
            "tNumber", binding.tvPaymentNumberValue.text.toString()
        )
        data.put("harga", binding.tvNominalValue.text.toString())
        data.put("status", "Pending")
        data.put("keterangan", requireContext().getString(R.string.pendingText))
        val requestBody = data.toString().toRequestBody("application/json".toMediaTypeOrNull())
        Log.d("Testing", requestBody.toString())
        if (photoParts.isNotEmpty()) {
            viewModel.createTransaction(photoParts, requestBody)
                .observe(viewLifecycleOwner) { data ->
                    when (data) {
                        is Event.Success -> {
                            Log.d("Testing", data.data.toString())
                            binding.apply {
                                progressBar.visibility = View.GONE
                                Snackbar.make(requireView(), "Berhasil upload", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            showMainView()
                        }

                        is Event.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        else -> {
                            binding.progressBar.visibility = View.GONE
                            Snackbar.make(requireView(), "Terjadi kesalahan", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("Testing", data.toString())
                        }
                    }
                }
        } else {
            Snackbar.make(
                requireView(),
                "Mohon masukkan gambar terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun showMainView() {
        binding.qrisLayout.visibility = View.VISIBLE
        binding.fragmentImage.visibility = View.GONE
        binding.qrisLayout.alpha = 1.0F
    }

    private fun showImageSourceDialog() {

        val options = arrayOf("File", "Galeri")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Ambil Gambar Menggunakan")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> openPdf()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openPdf() {
        binding.tvTitle.text = "Upload PDF"
        val intent = Intent()
        intent.setType("application/pdf")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_FILE)

    }

    private fun openGallery() {
        binding.tvTitle.text = "Upload Image"
        binding.ivImage.background = null
        val intent = Intent(MediaStore.ACTION_PICK_IMAGES).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            data?.data.let { uri ->
                val uri = data!!.data
                when (requestCode) {
                    PICK_PDF_FILE -> {
                        handleFile()
                    }

                    REQUEST_GALLERY -> {
                        image =
                            MediaStore.Images.Media.getBitmap(
                                requireActivity().contentResolver,
                                uri
                            )
                        handleImage()

                    }
                }
                if (uri != null && requestCode == REQUEST_GALLERY) {
                    SelectedPhoto(uri)
                } else if (uri != null && requestCode == PICK_PDF_FILE) {
                    selectedPdf(uri)
                }
            }
        }
    }

    private fun handleFile(){
        binding.fragmentImage.visibility = View.VISIBLE
        binding.qrisLayout.alpha = 0.6F
    }

    private fun selectedPdf(file: Uri) {
        try {
            val context = requireContext()
            val pdfFile = createFileFromUri(context, file)
            Log.d("Testing", pdfFile.toString())
            val requestFile: RequestBody = pdfFile.asRequestBody("application/pdf".toMediaTypeOrNull())
            val photoPart: MultipartBody.Part = MultipartBody.Part.createFormData("photos", pdfFile.name, requestFile)
            // Add photoPart to your request body or list
             photoParts.add(photoPart)
        } catch (e: Exception) {
            Log.e("Error", "File selection failed", e)
        }
    }

    private fun SelectedPhoto(photo: Uri) {
        photoParts.clear()
        val imageFile = File(photo.getRealPathFromUri(requireActivity(), requireContext()))
        val requestFile: RequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val photoPart: MultipartBody.Part =
            MultipartBody.Part.createFormData("photos", imageFile.name, requestFile)
        photoParts.add(photoPart)
    }

    private fun createFileFromUri(context: Context, uri: Uri): File {
        val contentResolver: ContentResolver = context.contentResolver
        val fileName = getFileName(contentResolver, uri)
        val file = File(context.cacheDir, fileName)
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return file
    }
    private fun getFileName(contentResolver: ContentResolver, uri: Uri): String {
        var name = "temp_file"
        val returnCursor = contentResolver.query(uri, null, null, null, null)
        returnCursor?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            if (nameIndex != -1) {
                name = cursor.getString(nameIndex)
            }
        }
        return name
    }
    private fun getRealPathFromUri(activity: Activity, uri: Uri): String {
        var path: String? = null
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = activity.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            cursor.moveToFirst()
            path = cursor.getString(column_index)
            cursor.close()
        }
        return path ?: uri.path.toString()
    }

    private fun handleImage() {
        binding.fragmentImage.visibility = View.VISIBLE
        binding.qrisLayout.alpha = 0.6F
        imageView = binding.ivImage
        imageView.setImageBitmap(image)

    }

    companion object {
        private const val REQUEST_CAMERA = 1
        private const val REQUEST_GALLERY = 2
        private const val PICK_PDF_FILE = 3

    }

}