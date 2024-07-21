package com.example.iuran_gss_2.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.iuran_gss_2.model.local.CreateRequest
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.model.local.LoginRequest
import com.example.iuran_gss_2.model.remote.LoginResponse
import com.example.iuran_gss_2.model.remote.SimpleResponse
import com.example.iuran_gss_2.repository.IuranRepository

class RegisterViewModel(val repository: IuranRepository) : ViewModel() {

    fun createUser(request: CreateRequest): LiveData<Event<SimpleResponse>> =
        repository.createUser(request)

    fun login(request: LoginRequest): LiveData<Event<LoginResponse>> = repository.getLogin(request)

    fun saveEncrypted(email: String, token: String, password: String) =
        repository.saveEncryptedValues(
            email = email,
            token = token,
            password = password,
            role = "User"
        )

}