package com.example.iuran_gss_2.ui.qris

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.databinding.FragmentQrisBinding
import com.google.android.material.snackbar.Snackbar


class QrisFragment : Fragment() {
    private lateinit var binding : FragmentQrisBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQrisBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigate()
    }

    private fun navigate () {
        binding.btnSend.setOnClickListener {
            Snackbar.make(requireView(), "Mengirim ...", Toast.LENGTH_SHORT).show()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}