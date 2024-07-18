package com.example.iuran_gss_2.ui.admin.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.iuran_gss_2.databinding.FragmentAktifitasBinding

class AktifitasFragment : Fragment() {

    private lateinit var binding: FragmentAktifitasBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAktifitasBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigate()
    }

    private fun navigate() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}