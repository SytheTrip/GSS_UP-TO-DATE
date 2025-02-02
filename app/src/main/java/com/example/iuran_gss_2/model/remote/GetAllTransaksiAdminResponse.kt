package com.example.iuran_gss_2.model.remote

import com.google.gson.annotations.SerializedName

data class GetAllTransaksiAdminResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: List<DataTransactionAdmin>,

	@field:SerializedName("message")
	val message: String
)

data class DataTransactionAdmin(

	@field:SerializedName("list")
	val list: ListTransactionAdmin
)

data class ListTransactionAdmin(

	@field:SerializedName("keterangan")
	val keterangan: String,

	@field:SerializedName("harga")
	val harga: String,

	@field:SerializedName("tNumber")
	val tNumber: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("createdAt")
	val createdAt: Long,

	@field:SerializedName("bukti")
	val bukti: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)
