package com.example.iuran_gss_2.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.databinding.FragmentRegisterBinding
import com.example.iuran_gss_2.utils.verifyRegisterInput
import com.google.android.material.snackbar.Snackbar


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigate()
    }

    private fun navigate() {
        binding.btnRegister.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val address = binding.addressInput.text.toString()
            val blok = binding.blocInput.text.toString()
            val handphone = binding.handphoneInput.text.toString()
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            if(verifyRegisterInput(name,address,blok,handphone,email,password)) {
                val toHome =  RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
                findNavController().navigate(toHome)
            } else {
                Snackbar.make(requireView(), "Harus di isi terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

}