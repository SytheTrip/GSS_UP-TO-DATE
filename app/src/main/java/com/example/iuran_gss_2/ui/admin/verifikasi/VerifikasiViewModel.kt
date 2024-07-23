package com.example.iuran_gss_2.ui.admin.verifikasi

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.model.local.TransaksiRequest
import com.example.iuran_gss_2.model.local.UpdateTransaksiRequest
import com.example.iuran_gss_2.model.remote.GeneralResponse
import com.example.iuran_gss_2.model.remote.GetTransaksiResponse
import com.example.iuran_gss_2.repository.IuranRepository

class VerifikasiViewModel(private val repository: IuranRepository) : ViewModel() {

    fun updateTransaksi(request: UpdateTransaksiRequest): LiveData<Event<GeneralResponse>> =
        repository.updateTransaksi(request)

    fun getTransaksi(request : TransaksiRequest): LiveData<Event<GetTransaksiResponse>> = repository.getTransaksi(request)


}