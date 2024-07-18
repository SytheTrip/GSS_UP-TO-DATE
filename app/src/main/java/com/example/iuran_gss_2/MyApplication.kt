package com.example.iuran_gss_2

import android.app.Application
import com.example.iuran_gss_2.di.encryptionModule
import com.example.iuran_gss_2.di.networkModule
import com.example.iuran_gss_2.di.repositoryModule
import com.example.iuran_gss_2.di.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext

class MyApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@MyApplication)
            modules(
                networkModule,
                viewModule,
                repositoryModule,
                encryptionModule,
            )
        }
    }
}