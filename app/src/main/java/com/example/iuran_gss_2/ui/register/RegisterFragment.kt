package com.example.iuran_gss_2.ui.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.iuran_gss_2.databinding.FragmentRegisterBinding
import com.example.iuran_gss_2.model.local.CreateRequest
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.model.local.LoginRequest
import com.example.iuran_gss_2.utils.verifyRegisterInput
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModel()
    private lateinit var nama: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var noRumah: String
    private lateinit var noPhone: String
    private var token: String = "Bearer "
    private lateinit var blok: String

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

    private fun observeData() {
        binding.apply {
            nama = nameInput.text.toString()
            password = passwordInput.text.toString()
            email = emailInput.text.toString()
            noRumah = noRumahInput.text.toString()
            blok = blocInput.text.toString()
            noPhone = handphoneInput.text.toString()
        }
        if (verifyRegisterInput(nama, password, blok, noPhone, email, password)) {

            val request = CreateRequest(
                email = email,
                password = password,
                nama = nama,
                noRumah = noRumah,
                blok = blok,
                noPhone = noPhone,
            )
            viewModel.createUser(request).observe(viewLifecycleOwner) { data ->
                when (data) {
                    is Event.Success -> {
                        binding.progressBar.visibility = View.GONE
                        if (data.data.msg == "Email sudah terdaftar") {
                            Snackbar.make(
                                requireView(),
                                "Email telah digunakan",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val loginRequest = LoginRequest(email, password)
                            goLogin(loginRequest)
                        }
                    }

                    is Event.Error -> {
                        Snackbar.make(
                            requireView(),
                            "Email telah digunakan",
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
        } else {
            Snackbar.make(requireView(), "Harus di isi terlebih dahulu", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun goLogin(request: LoginRequest) {
        viewModel.login(request).observe(viewLifecycleOwner) { data ->
            when (data) {
                is Event.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(requireView(), "Berhasil login", Toast.LENGTH_SHORT).show()
                    val dataAkun = data.data
                    token += dataAkun.token
                    viewModel.saveEncrypted(email, token, password)
                    goHome()
                }

                is Event.Error -> {
                    Snackbar.make(
                        requireView(),
                        "Terjadi kesalahan pada server",
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
        binding.btnRegister.setOnClickListener {
            observeData()
        }
    }

    private fun goHome() {
        val toHome = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
        findNavController().navigate(toHome)
    }


}