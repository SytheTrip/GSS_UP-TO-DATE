package com.example.iuran_gss_2.ui.qris

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.iuran_gss_2.model.local.CreateTransactionRequest
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.model.remote.SimpleResponse
import com.example.iuran_gss_2.repository.IuranRepository

class QrisViewModel(val repository: IuranRepository) : ViewModel() {

    fun createTransaction(request: CreateTransactionRequest): LiveData<Event<SimpleResponse>> =
        repository.createTransaction(request)
}