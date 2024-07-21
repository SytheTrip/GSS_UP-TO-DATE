package com.example.iuran_gss_2.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.model.remote.GetAllTransaksiUserResponse
import com.example.iuran_gss_2.repository.IuranRepository

class HistoryViewModel(private val repository: IuranRepository) : ViewModel() {

    private fun getAccount(): String = repository.getData().token.toString()

    fun getAllTransaction(): LiveData<Event<GetAllTransaksiUserResponse>> =
        repository.getAllTransaksiUser(getAccount())
}