package com.example.iuran_gss_2.model.remote

import com.google.gson.annotations.SerializedName

data class GetAllTransaksiUserResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: ListTransactionUser,

    @field:SerializedName("message")
    val message: String
)

data class DataTransaction(

    @field:SerializedName("tNumber")
    val tNumber: String,

    @field:SerializedName("keterangan")
    val keterangan: String,

    @field:SerializedName("harga")
    val harga: String,

    @field:SerializedName("namaLengkap")
    val namaLengkap: String,

    @field:SerializedName("status")
    val status: String
)

data class ListTransactionUser(

    @field:SerializedName("listHistory")
    val listHistory: List<ListHistoryItem>
)

data class ListHistoryItem(

    @field:SerializedName("data")
    val dataTransaction: DataTransaction
)
