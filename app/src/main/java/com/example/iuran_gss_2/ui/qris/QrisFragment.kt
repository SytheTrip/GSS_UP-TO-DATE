package com.example.iuran_gss_2.ui.qris

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.iuran_gss_2.databinding.FragmentQrisBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

@RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
class QrisFragment : Fragment() {
    private lateinit var binding: FragmentQrisBinding
    private val viewModel: QrisViewModel by viewModel()
    private lateinit var kode: String

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
            Snackbar.make(requireView(), "Ceritanya mengirim", Toast.LENGTH_SHORT).show()
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
        val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    handleImage(imageBitmap)
                }

                REQUEST_GALLERY -> {
                    val imageUri = data?.data
                    imageUri?.let {
                        val imageBitmap =
                            MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
                        handleImage(imageBitmap)
                    }
                }
            }
        }
    }

    private fun handleImage(image: Bitmap) {
        binding.fragmentImage.visibility = View.VISIBLE
        binding.qrisLayout.alpha = 0.6F
        val imageView: ImageView = binding.ivImage
        imageView.setImageBitmap(image)

    }

    companion object {
        private const val REQUEST_CAMERA = 1
        private const val REQUEST_GALLERY = 2
    }

}