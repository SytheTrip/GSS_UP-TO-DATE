package com.example.iuran_gss_2.utils

enum class PaymentCategory {
    IuranKeamanan,IuranSampah,UangKas
}

fun convertToString( category : PaymentCategory) : String{
    return when(category) {
        PaymentCategory.IuranKeamanan -> {
            "IK"
        }
        PaymentCategory.IuranSampah -> {
            "IS"
        }
        PaymentCategory.UangKas -> {
            "UK"
        }
    }
}