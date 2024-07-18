package com.example.iuran_gss_2.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.databinding.FragmentHomeBinding
import com.example.iuran_gss_2.utils.PaymentCategory
import com.example.iuran_gss_2.utils.convertToString
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigate();
    }

    private fun navigate() {
        binding.cardIuranKeamanan.setOnClickListener {
            val toIK = HomeFragmentDirections.actionHomeFragmentToQrisFragment(convertToString(category = PaymentCategory.IuranKeamanan))
            findNavController().navigate(toIK)
        }
        binding.cardIuranSampah.setOnClickListener {
            val toIS =  HomeFragmentDirections.actionHomeFragmentToQrisFragment(convertToString(category = PaymentCategory.IuranSampah))
            findNavController().navigate(toIS)
        }
        binding.cardUangKas.setOnClickListener {
            val toUK = HomeFragmentDirections.actionHomeFragmentToQrisFragment(convertToString(category = PaymentCategory.UangKas))
            findNavController().navigate(toUK)
        }
        binding.cardTransaksi.setOnClickListener {
            val toHistory = HomeFragmentDirections.actionHomeFragmentToHistoryFragment()
            findNavController().navigate(toHistory)
        }
        binding.cardBantuan.setOnClickListener {
            Intent(Intent.ACTION_VIEW)
            try {
                val phone = "081284783667"
                val url = "https://api.whatsapp.com/send?phone=+62$phone"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding.ivProfile.setOnClickListener {
            val toHistory = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
            findNavController().navigate(toHistory)
        }

    }

}