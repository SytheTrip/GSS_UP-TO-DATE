package com.example.iuran_gss_2.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.databinding.FragmentEditProfileBinding
import com.google.android.material.snackbar.Snackbar

class EditProfileFragment : Fragment() {
    private lateinit var binding : FragmentEditProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(layoutInflater,container,false)
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
        binding.btnSave.setOnClickListener {
            Snackbar.make(requireView(),"Berhasil menyimpan", Toast.LENGTH_SHORT).show()
        }
    }
}