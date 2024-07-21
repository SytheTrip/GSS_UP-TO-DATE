package com.example.iuran_gss_2.model.local

import com.google.gson.annotations.SerializedName

data class CreateRequest(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("noPhone")
	val noPhone: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("alamat")
	val alamat: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("blok")
	val blok: String,
)
