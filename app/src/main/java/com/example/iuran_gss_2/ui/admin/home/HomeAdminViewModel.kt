package com.example.iuran_gss_2.ui.admin.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.iuran_gss_2.model.local.DataHolder
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.model.remote.GetUsernameResponse
import com.example.iuran_gss_2.repository.IuranRepository

class HomeAdminViewModel(val repository: IuranRepository) : ViewModel() {
    private lateinit var account: DataHolder

    private fun getAccount() {
        account = repository.getData()
    }

    fun getUsername(): LiveData<Event<GetUsernameResponse>> {
        getAccount()
        return repository.getUsername(account.token.toString())
    }

}