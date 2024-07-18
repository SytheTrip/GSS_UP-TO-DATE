package com.example.iuran_gss_2.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.iuran_gss_2.databinding.FragmentProfileBinding
import com.google.android.material.snackbar.Snackbar


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigate()
    }

    private fun navigate () {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnEdit.setOnClickListener {
            Snackbar.make(requireView(), "Data tersimpan", Toast.LENGTH_SHORT).show()
        }
    }

}