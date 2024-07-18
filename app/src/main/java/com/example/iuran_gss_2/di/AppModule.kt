package com.example.iuran_gss_2.di

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.iuran_gss_2.network.ApiService
import com.example.iuran_gss_2.utils.BaseUrlInterceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {
    single { provideBaseUrlInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideApiService(get()) }
}

private fun provideBaseUrlInterceptor(): BaseUrlInterceptor {
    return BaseUrlInterceptor("baseUrl")
}

private fun provideOkHttpClient(baseUrlInterceptor: BaseUrlInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(baseUrlInterceptor)
        .build()
}

private fun provideApiService(okHttpClient: OkHttpClient): ApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl("baseURL") // URL default, tidak digunakan saat melakukan permintaan
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(ApiService::class.java)
}

val encryptionModule = module {
    single {
        val context: Context = androidContext()
        val masterKeyAlias = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "encrypted_pref",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}