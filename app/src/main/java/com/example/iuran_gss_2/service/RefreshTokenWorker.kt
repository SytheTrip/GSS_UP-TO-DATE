package com.example.iuran_gss_2.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.iuran_gss_2.repository.IuranRepository
import kotlinx.coroutines.delay
import org.koin.java.KoinJavaComponent.inject

class RefreshTokenWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    private val repository: IuranRepository by inject(IuranRepository::class.java)

    override suspend fun doWork(): Result {
        // Lakukan pemanggilan refreshToken() menggunakan instance repository atau apiService
        try {
            repository.refreshToken()
        } catch (e: Exception) {
            // Tangani kesalahan jika ada
            return Result.failure()
        }
        // Jeda selama satu jam sebelum tugas selesai
        delay(60 * 60 * 1000) // Satu jam dalam milidetik

        // Kembalikan Result.success() untuk menandakan tugas berhasil selesai
        return Result.success()
    }
}