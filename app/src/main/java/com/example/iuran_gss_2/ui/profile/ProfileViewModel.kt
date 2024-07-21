package com.example.iuran_gss_2.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.iuran_gss_2.model.local.DataHolder
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.model.remote.Data
import com.example.iuran_gss_2.model.remote.ProfileResponse
import com.example.iuran_gss_2.repository.IuranRepository

class ProfileViewModel(private val repository: IuranRepository): ViewModel() {

    fun getProfile(token : String) : LiveData<Event<ProfileResponse>> = repository.getProfile(token)

    fun logout() = repository.logOut()
    fun getData() : DataHolder = repository.getData()
}