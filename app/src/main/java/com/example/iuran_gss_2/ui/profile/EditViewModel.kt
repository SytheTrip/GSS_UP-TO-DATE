package com.example.iuran_gss_2.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.iuran_gss_2.model.local.DataHolder
import com.example.iuran_gss_2.model.local.EditProfileRequest
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.model.remote.GeneralResponse
import com.example.iuran_gss_2.model.remote.ProfileResponse
import com.example.iuran_gss_2.repository.IuranRepository

class EditViewModel(private val repository: IuranRepository) : ViewModel() {
    private lateinit var account: DataHolder
    private fun getData(): DataHolder = repository.getData()

    fun getProfile(): LiveData<Event<ProfileResponse>> {
        account = getData()
        return repository.getProfile(account.token.toString())
    }

    fun editProfile(request: EditProfileRequest): LiveData<Event<GeneralResponse>> =
        repository.editProfile(account.token.toString(), request)
}