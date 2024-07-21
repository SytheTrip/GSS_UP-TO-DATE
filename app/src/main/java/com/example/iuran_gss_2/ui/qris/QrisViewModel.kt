package com.example.iuran_gss_2.ui.qris

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.iuran_gss_2.model.local.CreateTransactionRequest
import com.example.iuran_gss_2.model.local.DataHolder
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.model.remote.SimpleResponse
import com.example.iuran_gss_2.repository.IuranRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class QrisViewModel(private val repository: IuranRepository) : ViewModel() {
    private lateinit var account: DataHolder
    fun createTransaction(
        photos: List<MultipartBody.Part>,
        request: RequestBody
    ): LiveData<Event<SimpleResponse>> {
        getData()
        return repository.createTransaction(account.token.toString(), photos, request)
    }

    private fun getData() {
        account = repository.getData()
    }
}