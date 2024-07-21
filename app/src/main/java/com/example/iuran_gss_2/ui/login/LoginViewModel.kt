package com.example.iuran_gss_2.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.model.local.LoginRequest
import com.example.iuran_gss_2.model.remote.GetUsernameResponse
import com.example.iuran_gss_2.model.remote.LoginResponse
import com.example.iuran_gss_2.repository.IuranRepository

class LoginViewModel(private val repository: IuranRepository) : ViewModel() {

    fun login(request : LoginRequest) : LiveData<Event<LoginResponse>> = repository.getLogin(request)

    fun setOnBoarding(completed : Boolean){
        repository.saveOnboarding(completed)
    }


    fun saveEncrypted(email: String, password: String, token: String,role : String) {
        repository.saveEncryptedValues(email, password, token,role)
        Log.d("Testing","Berhasil menyimpan $email $password ")
    }
}