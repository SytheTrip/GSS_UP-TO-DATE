package com.example.iuran_gss_2.ui.admin.verifikasi

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.iuran_gss_2.model.local.DataHolder
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.model.remote.GetAllTransaksiAdminResponse
import com.example.iuran_gss_2.repository.IuranRepository

class ListVerifikasiViewModel(private val repository: IuranRepository) : ViewModel() {

    private fun getData() = repository.getData()
    fun getListVerifikasi() : LiveData<Event<GetAllTransaksiAdminResponse>> = repository.getAllTransaksiAdmin(getData().token.toString())
}