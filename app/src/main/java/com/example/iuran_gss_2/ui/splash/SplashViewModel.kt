package com.example.iuran_gss_2.ui.splash

import androidx.lifecycle.ViewModel
import com.example.iuran_gss_2.repository.IuranRepository

class SplashViewModel(val repository: IuranRepository) : ViewModel() {

    fun getOnboarding(): Boolean = repository.getOnboarding()

    fun getRole(): String? = repository.getRole()

}