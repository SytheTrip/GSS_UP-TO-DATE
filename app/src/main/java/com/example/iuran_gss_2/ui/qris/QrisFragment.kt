package com.example.iuran_gss_2.ui.qris

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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
import com.example.iuran_gss_2.model.local.CreateTransactionRequest
import com.example.iuran_gss_2.model.local.Event
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
        navigate()
        setData()
    }

    private fun setData() {
        binding.apply {
            tvPaymentNumberValue.text = generateRandomCode()
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
            Snackbar.make(requireView(), "Ceritanya mengirim", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadTransaction() {
        data.put(
            "tNumber", binding.tvPaymentNumberValue.text.toString()
        )
        data.put("harga", binding.tvNominalValue.text.toString())
        data.put("status", "Pending")
        data.put("keterangan",requireContext ().getString(R.string.pendingText))
        val requestBody = data.toString().toRequestBody("application/json".toMediaTypeOrNull())
        Log.d("Testing", requestBody.toString())
        viewModel.createTransaction(photoParts, requestBody).observe(viewLifecycleOwner) { data ->
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
    }


    private fun showMainView() {
        binding.qrisLayout.visibility = View.VISIBLE
        binding.fragmentImage.visibility = View.GONE
        binding.qrisLayout.alpha = 1.0F
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Kamera", "Galeri")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Ambil Gambar Menggunakan")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    private fun openGallery() {
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
                when (requestCode) {
                    REQUEST_CAMERA -> {
                        image = data?.extras?.get("data") as Bitmap
                        handleImage()
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
                if (uri != null) {
                    SelectedPhoto(uri)
                }
            }
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

    private fun handleImage() {
        binding.fragmentImage.visibility = View.VISIBLE
        binding.qrisLayout.alpha = 0.6F
        imageView = binding.ivImage
        imageView.setImageBitmap(image)

    }

    companion object {
        private const val REQUEST_CAMERA = 1
        private const val REQUEST_GALLERY = 2
    }

}