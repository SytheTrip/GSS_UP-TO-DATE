package com.example.iuran_gss_2.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.model.remote.GetUsernameResponse
import com.example.iuran_gss_2.repository.IuranRepository

class HomeViewModel(private val repository: IuranRepository): ViewModel() {

    fun getData() = repository.getData()
    fun getUsername(token : String): LiveData<Event<GetUsernameResponse>> = repository.getUsername(token)

}