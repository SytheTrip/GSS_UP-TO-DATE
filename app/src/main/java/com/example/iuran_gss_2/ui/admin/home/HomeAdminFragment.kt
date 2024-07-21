package com.example.iuran_gss_2.ui.admin.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.databinding.FragmentHomeAdminBinding
import com.example.iuran_gss_2.model.local.Event
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeAdminFragment : Fragment() {
    private lateinit var binding: FragmentHomeAdminBinding
    private val viewModel: HomeAdminViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigate()
    }

    private fun navigate() {

        binding.tvAktivitas.setOnClickListener {
            val toAktivitas =
                HomeAdminFragmentDirections.actionHomeAdminFragmentToAktifitasFragment()
            findNavController().navigate(toAktivitas)
        }

        binding.tvMemberiInformasi.setOnClickListener {
            val toNotif =
                HomeAdminFragmentDirections.actionHomeAdminFragmentToSendNotificationFragment()
            findNavController().navigate(toNotif)
        }
        binding.cardName.setOnClickListener {
            val toProfile = HomeAdminFragmentDirections.actionHomeAdminFragmentToProfileFragment()
            findNavController().navigate(toProfile)
        }

        binding.tvUangKas.setOnClickListener {
            val toList =
                HomeAdminFragmentDirections.actionHomeAdminFragmentToListVerifikasiFragment()
            findNavController().navigate(toList)
        }

    }

}