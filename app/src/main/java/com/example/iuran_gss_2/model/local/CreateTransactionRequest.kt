package com.example.iuran_gss_2.model.local

import com.google.gson.annotations.SerializedName

data class CreateTransactionRequest(

	@field:SerializedName("tNumber")
	val tNumber: String,

	@field:SerializedName("harga")
	val harga: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("keterangan")
	val keterangan: String,

)
