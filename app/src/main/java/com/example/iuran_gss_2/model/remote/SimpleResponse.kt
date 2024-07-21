package com.example.iuran_gss_2.model.remote

import com.google.gson.annotations.SerializedName

data class SimpleResponse(

    @field:SerializedName("code")
    val code: String,

    @field:SerializedName("msg")
    val msg: String
)
