package com.example.iuran_gss_2.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.databinding.FragmentEditProfileBinding
import com.example.iuran_gss_2.model.local.EditProfileRequest
import com.example.iuran_gss_2.model.local.Event
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel: EditViewModel by viewModel()
    private lateinit var name: String
    private lateinit var noRumah: String
    private lateinit var bloc: String
    private lateinit var noPhone: String
    private lateinit var email: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigate()
        getProfile()
    }

    private fun getProfile() {
        viewModel.getProfile().observe(viewLifecycleOwner) { data ->
            when (data) {
                is Event.Success -> {
                    val dataProfile = data.data.data
                    binding.apply {
                        emailLayout.text = dataProfile.email
                        nameLayout.setText(dataProfile.namaLengkap)
                        addressLayout.setText(dataProfile.noRumah)
                        blocLayout.setText(dataProfile.blok)
                        handphoneLayout.setText(dataProfile.noPhone)
                        progressBar.visibility = View.GONE
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

    private fun editProfile() {
        binding.apply {
            name = nameLayout.text.toString()
            noRumah = addressLayout.text.toString()
            bloc = blocLayout.text.toString()
            noPhone = handphoneLayout.text.toString()
        }
        if (name.isNotEmpty() && noRumah.isNotEmpty() && bloc.isNotEmpty() && noPhone.isNotEmpty()) {
            val request = EditProfileRequest(
                namaLengkap = name,
                blok = bloc,
                noPhone = noPhone,
                noRumah = noRumah
            )
            viewModel.editProfile(request).observe(viewLifecycleOwner) { data ->
                when (data) {
                    is Event.Success -> {
                        binding.apply {
                            progressBar.visibility = View.GONE
                            Snackbar.make(
                                requireView(),
                                "Data berhasil diupdate",
                                Toast.LENGTH_SHORT
                            ).show()
                            goHome()
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
        } else {
            Snackbar.make(requireView(), "Isi terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigate() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnSave.setOnClickListener {
            editProfile()
        }
    }

    private fun goHome() {
        val toHome = EditProfileFragmentDirections.actionEditProfileFragmentToHomeFragment()
        findNavController().navigate(toHome)
    }
}