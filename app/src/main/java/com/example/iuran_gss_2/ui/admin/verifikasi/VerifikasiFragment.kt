package com.example.iuran_gss_2.ui.admin.verifikasi

import android.app.ProgressDialog
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.databinding.FragmentVerifikasiBinding
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.model.local.TransaksiRequest
import com.example.iuran_gss_2.model.local.UpdateTransaksiRequest
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection


class VerifikasiFragment : Fragment() {
    private lateinit var binding: FragmentVerifikasiBinding
    private val viewModel: VerifikasiViewModel by viewModel()
    private lateinit var nama: String
    private lateinit var tNumber: String
    private lateinit var noPembayaran: String
    private lateinit var noRumah: String
    private lateinit var noPhone: String
    private lateinit var email: String
    private lateinit var nominal: String
    private lateinit var keterangan: String
    private lateinit var keteranganInput: String
    private lateinit var fileType: String
    private lateinit var status: String
    private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifikasiBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tNumber = arguments?.getString("tNumber").toString()
        navigate()
        setupText()
    }


    private fun navigate() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnAccept.setOnClickListener {
            status = "Diterima"
            updateTransaksi()
        }
        binding.btnReject.setOnClickListener {
            status = "Ditolak"
            updateTransaksi()
        }
    }

    private fun setupText() {
        nama = requireContext().getString(R.string.nama_input)
        noPembayaran = requireContext().getString(R.string.pembayaran_input)
        noRumah = requireContext().getString(R.string.blok_input)
        noPhone = requireContext().getString(R.string.telepon_input)
        email = requireContext().getString(R.string.email_input)
        nominal = requireContext().getString(R.string.nominal_input)
        keterangan = requireContext().getString(R.string.keterangan_input)
        observeData()
    }

    private fun observeData() {
        val request = TransaksiRequest(tNumber = tNumber)

        viewModel.getTransaksi(request).observe(viewLifecycleOwner) { data ->
            when (data) {
                is Event.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val dataTransaksi = data.data.data
                    binding.apply {
                        tvNama.text = ("$nama ${dataTransaksi.username}")
                        tvNoPembayaran.text = ("$noPembayaran ${dataTransaksi.tNumber}")
                        tvBlok.text = ("$noRumah ${dataTransaksi.noRumah} / ${dataTransaksi.blok} ")
                        tvTelpon.text = ("$noPhone ${dataTransaksi.noPhone}")
                        tvEmail.text = ("$email ${dataTransaksi.email}")
                        tvNominal.text = ("$nominal ${dataTransaksi.harga}")
                        tvKeterangan.text = ("$keterangan ${dataTransaksi.keterangan}")
                        fileType = dataTransaksi.fileType
                        if (fileType == "image") {
                            binding.tvDownload.visibility = View.GONE
                            Glide.with(requireActivity())
                                .load(dataTransaksi.bukti)
                                .into(ivImage)
                        } else {
                            binding.tvDownload.setOnClickListener {
                                binding.webLayout.visibility = View.VISIBLE
                                viewPdf(dataTransaksi.bukti)
                                binding.wvBack.setOnClickListener {
                                    binding.webLayout.visibility = View.GONE
                                }
                            }
                        }
                    }
                }

                is Event.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(
                        requireView(),
                        requireContext().getString(R.string.something_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("Event ", data.toString())
                }
            }
        }
    }

    private fun viewPdf(bukti: String) {
        try {
            val uri = Uri.parse(bukti)
            val url = URLEncoder.encode(bukti, "UTF-8")
            RetrievePDFfromUrl(binding.wvPdf).execute(uri.toString())

//            binding.wvPdf.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }


    private fun updateTransaksi() {
        keteranganInput = binding.inputKeterangan.editText?.text.toString()
        val request =
            UpdateTransaksiRequest(tNumber = tNumber, status = status, keterangan = keteranganInput)
        viewModel.updateTransaksi(request).observe(viewLifecycleOwner) { data ->
            when (data) {
                is Event.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(requireView(), "Status berhasil diupdate", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigateUp()
                }

                is Event.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(
                        requireView(),
                        requireContext().getString(R.string.something_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("Event ", data.toString())
                }
            }
        }
    }

    private inner class RetrievePDFfromUrl(private val pdfView: PDFView) :
        AsyncTask<String?, Void?, InputStream?>() {

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(requireContext())
            progressDialog.setMessage("Downloading PDF...")
            progressDialog.isIndeterminate = true
            progressDialog.setCancelable(false)
            progressDialog.show()
        }

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
            progressDialog.dismiss()
            if (inputStream != null) {
                pdfView.fromStream(inputStream).load()
            } else {
                // Handle error
                showErrorDialog()
            }
        }

        private fun showErrorDialog() {
            val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            builder.setTitle("Error")
            builder.setMessage("Failed to download PDF. Please try again.")
            builder.setPositiveButton("OK", null)
            builder.show()
        }
    }
}