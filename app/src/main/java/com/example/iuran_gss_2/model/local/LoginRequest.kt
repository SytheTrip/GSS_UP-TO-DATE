package com.example.iuran_gss_2.model.local

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("password")
    val password: String? = null
)
