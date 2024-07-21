package com.example.iuran_gss_2.model.remote

import com.google.gson.annotations.SerializedName

data class GetAllTransaksiAdminResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: Map<String, DataTransaksi>,

	@field:SerializedName("message")
	val message: String
)

data class DataTransaksi(

	@field:SerializedName("createdAt")
	val createdAt: Long,

	@field:SerializedName("keterangan")
	val keterangan: String,

	@field:SerializedName("User")
	val user: String,

	@field:SerializedName("harga")
	val harga: String,

	@field:SerializedName("bukti")
	val bukti: String,

	@field:SerializedName("transactionNumber")
	val transactionNumber: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("username")
	val username: String
)
