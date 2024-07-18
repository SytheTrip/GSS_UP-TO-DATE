package com.example.iuran_gss_2.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.iuran_gss_2.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigate()
    }

    private fun navigate() {
        binding.btnLogin.setOnClickListener {
            val toHome = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
            findNavController().navigate(toHome)
        }
        binding.btnAdmin.setOnClickListener {
            val toAdmin = LoginFragmentDirections.actionLoginFragmentToHomeAdminFragment()
            findNavController().navigate(toAdmin)
        }
    }

}