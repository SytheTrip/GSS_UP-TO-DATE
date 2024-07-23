package com.example.iuran_gss_2.utils

import java.text.NumberFormat
import java.util.Locale


enum class PaymentCategory {
    IuranKeamanan,
    IuranSampah,
    UangKas

}

fun convertToString(category: PaymentCategory): String {
    return when (category) {
        PaymentCategory.IuranKeamanan -> "IK"
        PaymentCategory.IuranSampah -> "IS"
        PaymentCategory.UangKas -> "UK"
    }
}

fun getPrice(category: String): Int {
    return when (category) {
        "IK" -> 100000
        "IS" -> 25000
        "UK" -> 10000
        else -> {
            0
        }
    }
}

fun formatPrice(price: Int): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN)
    return numberFormat.format(price)
}