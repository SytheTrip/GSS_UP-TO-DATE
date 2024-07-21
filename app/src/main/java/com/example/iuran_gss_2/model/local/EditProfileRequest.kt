package com.example.iuran_gss_2.model.local

import com.google.gson.annotations.SerializedName

data class EditProfileRequest(

	@field:SerializedName("noRumah")
	val noRumah: String,

	@field:SerializedName("noPhone")
	val noPhone: String,

	@field:SerializedName("blok")
	val blok: String,

	@field:SerializedName("namaLengkap")
	val namaLengkap: String
)
