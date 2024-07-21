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
import com.example.iuran_gss_2.databinding.FragmentProfileBinding
import com.example.iuran_gss_2.model.local.DataHolder
import com.example.iuran_gss_2.model.local.Event
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModel()
    private lateinit var account: DataHolder

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
        account = viewModel.getData()
        observeData()
    }

    private fun observeData() {
        viewModel.getProfile(account.token.toString()).observe(viewLifecycleOwner) { data ->
            when (data) {
                is Event.Success -> {
                    val dataProfile = data.data.data
                    binding.apply {
                        emailLayout.text = dataProfile.email
                        nameLayout.text = dataProfile.namaLengkap
                        addressLayout.text = dataProfile.noRumah
                        blocLayout.text = dataProfile.blok
                        handphoneLayout.text = dataProfile.noPhone
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

    private fun navigate() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnEdit.setOnClickListener {
            val toEdit = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
            findNavController().navigate(toEdit)
        }
    }

}