package com.example.iuran_gss_2.di


import com.example.iuran_gss_2.repository.IuranRepository
import com.example.iuran_gss_2.ui.admin.activity.AktifitasViewModel
import com.example.iuran_gss_2.ui.admin.home.HomeAdminViewModel
import com.example.iuran_gss_2.ui.admin.verifikasi.ListVerifikasiViewModel
import com.example.iuran_gss_2.ui.admin.verifikasi.VerifikasiViewModel
import com.example.iuran_gss_2.ui.history.HistoryViewModel
import com.example.iuran_gss_2.ui.home.HomeViewModel
import com.example.iuran_gss_2.ui.login.LoginViewModel
import com.example.iuran_gss_2.ui.profile.EditViewModel
import com.example.iuran_gss_2.ui.profile.ProfileViewModel
import com.example.iuran_gss_2.ui.qris.QrisViewModel
import com.example.iuran_gss_2.ui.register.RegisterViewModel
import com.example.iuran_gss_2.ui.send_notification.SendNotificationViewModel
import com.example.iuran_gss_2.ui.splash.SplashViewModel
import org.koin.dsl.module


val repositoryModule = module {
    single { IuranRepository(get(), get(), get()) }
}

val viewModule = module {
    single { LoginViewModel(get()) }
    single { HomeViewModel(get()) }
    single { ProfileViewModel(get()) }
    single { EditViewModel(get()) }
    single { AktifitasViewModel(get()) }
    single { HomeAdminViewModel(get()) }
    single { HistoryViewModel(get()) }
    single { ListVerifikasiViewModel(get()) }
    single { VerifikasiViewModel(get()) }
    single { QrisViewModel(get()) }
    single { SendNotificationViewModel(get()) }
    single { RegisterViewModel(get()) }
    single { SplashViewModel(get()) }


}
