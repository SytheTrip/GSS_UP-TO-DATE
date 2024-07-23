package com.example.iuran_gss_2.ui.admin.verifikasi

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
import com.example.iuran_gss_2.utils.PDFTools
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

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
                            downloadPDF(dataTransaksi.bukti)
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

    private fun downloadPDF(url: String) {
        binding.tvDownload.setOnClickListener {
            Log.d("Testing",url)
            PDFTools().downloadAndOpenPDF(requireContext(), url)
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
}