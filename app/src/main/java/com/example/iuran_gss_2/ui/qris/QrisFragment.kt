package com.example.iuran_gss_2.ui.qris

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.databinding.FragmentQrisBinding


class QrisFragment : Fragment() {
    private lateinit var binding : FragmentQrisBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQrisBinding.inflate(inflater,container,false)
        return binding.root
    }

}