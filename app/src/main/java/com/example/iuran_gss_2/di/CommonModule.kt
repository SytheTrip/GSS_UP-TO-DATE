package com.example.iuran_gss_2.di


import com.example.iuran_gss_2.repository.IuranRepository
import org.koin.dsl.module


val repositoryModule = module {
    single { IuranRepository(get())}
}

val viewModule = module {

}
