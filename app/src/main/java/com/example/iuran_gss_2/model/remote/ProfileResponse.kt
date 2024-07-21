package com.example.iuran_gss_2.model.remote

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String
)

data class Data(

	@field:SerializedName("noRumah")
	val noRumah: String,

	@field:SerializedName("noPhone")
	val noPhone: String,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("blok")
	val blok: String,

	@field:SerializedName("history")
	val history: List<String>,

	@field:SerializedName("namaLengkap")
	val namaLengkap: String,

	@field:SerializedName("email")
	val email: String
)
