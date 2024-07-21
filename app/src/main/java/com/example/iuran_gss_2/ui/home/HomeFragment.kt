package com.example.iuran_gss_2.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.databinding.FragmentHomeBinding
import com.example.iuran_gss_2.model.local.DataHolder
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.service.RefreshTokenWorker
import com.example.iuran_gss_2.utils.PaymentCategory
import com.example.iuran_gss_2.utils.convertToString
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var account: DataHolder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        account = viewModel.getData()
        navigate();
        observeData()
    }

    private fun startWork() {
        startRefreshTokenWorker()
    }

    private fun startRefreshTokenWorker() {
        val refreshTokenWorkRequest = PeriodicWorkRequestBuilder<RefreshTokenWorker>(
            1,
            TimeUnit.HOURS
        )
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "refreshTokenWork",
            ExistingPeriodicWorkPolicy.UPDATE,
            refreshTokenWorkRequest
        )
    }

    fun getGreeting(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 0..11 -> "Selamat pagi"
            in 12..14 -> "Selamat siang"
            in 15..17 -> "Selamat sore"
            else -> "Selamat malam"
        }
    }

    private fun stopWork() {
        stopRefreshTokenWorker()
    }


    private fun stopRefreshTokenWorker() {
        WorkManager.getInstance(requireContext()).cancelUniqueWork("refreshTokenWork")
    }


    private fun observeData() {
        if (account.email.isNullOrEmpty() || account.password.isNullOrEmpty()) {
            stopWork()
        } else {
            startWork()
            binding.tvSelamat.text = getGreeting()
            getUsername()
        }

    }

    private fun getUsername() {
        if (account.token.isNullOrEmpty()) {
            account = viewModel.getData()
            getUsername()
        } else {
            viewModel.getUsername(account.token.toString()).observe(viewLifecycleOwner) { data ->
                when (data) {
                    is Event.Success -> {

                        binding.progressBar.visibility = View.GONE
                        binding.tvName.text = data.data.data.toString()
                        Log.d("Testing", data.toString())
                    }

                    is Event.Error -> {
                        Snackbar.make(
                            requireView(),
                            requireContext().getString(R.string.invalid_login),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        binding.progressBar.visibility = View.VISIBLE
                        Log.d("Event ", data.toString())
                    }
                }
            }
        }
    }


    private fun navigate() {
        binding.cardIuranKeamanan.setOnClickListener {
            val toIK =
                HomeFragmentDirections.actionHomeFragmentToQrisFragment(convertToString(category = PaymentCategory.IuranKeamanan))
            findNavController().navigate(toIK)
        }
        binding.cardIuranSampah.setOnClickListener {
            val toIS =
                HomeFragmentDirections.actionHomeFragmentToQrisFragment(convertToString(category = PaymentCategory.IuranSampah))
            findNavController().navigate(toIS)
        }
        binding.cardUangKas.setOnClickListener {
            val toUK =
                HomeFragmentDirections.actionHomeFragmentToQrisFragment(convertToString(category = PaymentCategory.UangKas))
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
        binding.cardName.setOnClickListener {
            val toHistory = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
            findNavController().navigate(toHistory)
        }

    }

}