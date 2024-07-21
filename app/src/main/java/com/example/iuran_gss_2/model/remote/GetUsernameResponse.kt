package com.example.iuran_gss_2.model.remote

import com.google.gson.annotations.SerializedName

data class GetUsernameResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: String
)
