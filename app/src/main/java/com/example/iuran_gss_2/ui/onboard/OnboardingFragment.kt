package com.example.iuran_gss_2.ui.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.iuran_gss_2.databinding.FragmentOnboardingBinding


class OnboardingFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigate()
    }
    private fun navigate() {
        binding.btnLogin.setOnClickListener {
            OnboardingFragmentDirections.actionOnboardingFragmentToLoginFragment()
        }
        binding.btnRegister.setOnClickListener {
            OnboardingFragmentDirections.actionOnboardingFragmentToRegisterFragment()
        }
    }

}