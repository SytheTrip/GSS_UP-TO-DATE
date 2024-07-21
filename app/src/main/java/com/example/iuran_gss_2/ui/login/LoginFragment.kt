package com.example.iuran_gss_2.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.databinding.FragmentLoginBinding
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.model.local.LoginRequest
import com.example.iuran_gss_2.utils.verifyLoginInput
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private var token: String = "Bearer "
    private val viewModel: LoginViewModel by viewModel()

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
        observeData()
    }

    private fun observeData() {
        binding.btnLogin.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val request = LoginRequest(
                email = email, password = password
            )
            if (verifyLoginInput(email, password)) {
                login(request, email, password)
            } else {
                Snackbar.make(requireView(), "Harus di isi terlebih dahulu", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun login(request: LoginRequest, email: String, password: String) {
        viewModel.login(request).observe(viewLifecycleOwner) { data ->
            when (data) {
                is Event.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val dataAkun = data.data
                    token += dataAkun.token
                    viewModel.saveEncrypted(email, password, token)
                    viewModel.setOnBoarding(true)
                    if (dataAkun.status == "User") {
                        goHome()
                    } else {
                        goAdmin()
                    }
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
                    Log.d("Event ", data.toString())
                }
            }
        }
    }

    private fun navigate() {
        binding.btnLogin.setOnClickListener {
            val toHome = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
            findNavController().navigate(toHome)
        }
    }

    private fun goHome() {
        val toHome = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        findNavController().navigate(toHome)
    }

    private fun goAdmin() {
        val toAdmin = LoginFragmentDirections.actionLoginFragmentToHomeAdminFragment()
        findNavController().navigate(toAdmin)
    }

}