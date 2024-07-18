package com.example.iuran_gss_2.ui.admin.verifikasi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.databinding.FragmentVerifikasiBinding

class VerifikasiFragment : Fragment() {
    private lateinit var binding : FragmentVerifikasiBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifikasiBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}