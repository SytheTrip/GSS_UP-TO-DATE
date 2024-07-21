package com.example.iuran_gss_2.ui.admin.verifikasi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.databinding.FragmentListVerifikasiBinding
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.model.remote.DataTransactionAdmin
import com.example.iuran_gss_2.ui.adapter.ListVerifAdapter
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListVerifikasiFragment : Fragment() {
    private val viewModel: ListVerifikasiViewModel by viewModel()
    private lateinit var binding: FragmentListVerifikasiBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListVerifikasiBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigate()
        observeData()
    }

    private fun observeData() {
        viewModel.getListVerifikasi().observe(viewLifecycleOwner) { event ->
            when (event) {
                is Event.Success -> {
                    Log.d("Testing", "hasil event ${event.data}")
                    binding.progressBar.visibility = View.GONE
                    val transactionMap = event.data.data
                    setupAdapter(transactionMap)
                }

                is Event.Error -> {
                    Snackbar.make(
                        requireView(),
                        requireContext().getString(R.string.invalid_login),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar.visibility = View.GONE

                }

                else -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("Event ", event.toString())
                }
            }
        }
    }

    private fun navigate() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupAdapter(list: List<DataTransactionAdmin>) {
        val linearLayout = LinearLayoutManager(requireContext())
        val adapter = ListVerifAdapter(list)
        binding.rvPembayaran.layoutManager = linearLayout
        binding.rvPembayaran.adapter = adapter
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object :
            ListVerifAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataTransactionAdmin) {
                val toVerification = ListVerifikasiFragmentDirections.actionListVerifikasiFragmentToVerifikasiFragment(data.list.tNumber)
                findNavController().navigate(toVerification)
            }
        })
    }


}