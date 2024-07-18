package com.example.iuran_gss_2.utils

import android.view.View
import android.widget.Toast
import com.example.iuran_gss_2.ui.register.RegisterFragmentDirections
import com.google.android.material.snackbar.Snackbar


fun verifyRegisterInput(
    name: String,
    address: String,
    blok: String,
    handphone: String,
    email: String,
    password: String,
): Boolean {
    return if (!name.isNullOrEmpty() && !address.isNullOrEmpty() && !blok.isNullOrEmpty() && !handphone.isNullOrEmpty() && !email.isNullOrEmpty() && !password.isNullOrEmpty()) {
        true
    } else {
        false
    }
}