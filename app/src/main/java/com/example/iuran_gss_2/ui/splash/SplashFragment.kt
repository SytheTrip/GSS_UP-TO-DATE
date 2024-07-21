package com.example.iuran_gss_2.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.iuran_gss_2.databinding.FragmentSplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private val viewModel: SplashViewModel by viewModel()
    private lateinit var role: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val onBoarding = viewModel.getOnboarding()
        role = viewModel.getRole().toString()
        CoroutineScope(Dispatchers.Main).launch {
            delay(DURATION)
            if (onBoarding) {
                when (role) {
                    "User" -> {
                        toHomeUser()
                    }

                    "Admin" -> {
                        toHomeAdmin()
                    }

                    else -> {
                        toOnBoarding()
                    }
                }
            } else {
                toOnBoarding()
            }
        }
    }

    private fun toHomeUser() {
        val toHome = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
        findNavController().navigate(toHome)
    }

    private fun toOnBoarding() {
        val toOnboarding =
            SplashFragmentDirections.actionSplashFragmentToOnboardingFragment()
        findNavController().navigate(toOnboarding)
    }

    private fun toHomeAdmin() {
        val toHomeAdmin = SplashFragmentDirections.actionSplashFragmentToHomeAdminFragment()
        findNavController().navigate(toHomeAdmin)
    }

    companion object {
        const val DURATION = 1000L
    }
}