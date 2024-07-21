package com.example.iuran_gss_2.ui.history

import android.annotation.SuppressLint
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
import com.example.iuran_gss_2.databinding.FragmentHistoryBinding
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.model.remote.ListHistoryItem
import com.example.iuran_gss_2.ui.adapter.HistoryAdapter
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigate()
        observeData()
    }

    private fun navigate() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeData() {
        viewModel.getAllTransaction().observe(viewLifecycleOwner) { event ->
            when (event) {
                is Event.Success -> {
                    Log.d("Testing", "hasil event ${event.data}")
                    binding.progressBar.visibility = View.GONE
                    val transactionMap = event.data.data
                    Log.d("Testinng", "Hasil data $transactionMap")
                    setupAdapter(transactionMap.listHistory)

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

    @SuppressLint("NotifyDataSetChanged")
    private fun setupAdapter(list: List<ListHistoryItem>) {
        val linearLayout = LinearLayoutManager(requireContext())
        val adapter = HistoryAdapter(list)
        binding.rvHistory.layoutManager = linearLayout
        binding.rvHistory.adapter = adapter
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object :
            HistoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListHistoryItem) {
                Intent(Intent.ACTION_VIEW)
                try {
                    val phone = "081284783667"
                    val url = "https://api.whatsapp.com/send?phone=+62$phone"
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
        )
    }

}