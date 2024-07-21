package com.example.iuran_gss_2.model.local

import com.google.gson.annotations.SerializedName


data class UpdateTransaksiRequest(

    @field:SerializedName("tNumber")
    val tNumber: String,

    @field:SerializedName("status")
    val status: String,
)
