package com.example.iuran_gss_2.model.remote

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("msg")
    val msg: String,

    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("status")
    val status: String
)
