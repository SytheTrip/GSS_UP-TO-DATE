package com.example.iuran_gss_2.network

import com.example.iuran_gss_2.model.local.CreateRequest
import com.example.iuran_gss_2.model.local.EditProfileRequest
import com.example.iuran_gss_2.model.local.LoginRequest
import com.example.iuran_gss_2.model.local.TransaksiRequest
import com.example.iuran_gss_2.model.local.UpdateTransaksiRequest
import com.example.iuran_gss_2.model.remote.GeneralResponse
import com.example.iuran_gss_2.model.remote.GetAllTransaksiAdminResponse
import com.example.iuran_gss_2.model.remote.GetAllTransaksiUserResponse
import com.example.iuran_gss_2.model.remote.GetTransaksiResponse
import com.example.iuran_gss_2.model.remote.GetUsernameResponse
import com.example.iuran_gss_2.model.remote.LoginResponse
import com.example.iuran_gss_2.model.remote.ProfileResponse
import com.example.iuran_gss_2.model.remote.SimpleResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ApiService {

    @POST("login")
    suspend fun login(
        @Body body: LoginRequest
    ): Response<LoginResponse>

    @POST("createUser")
    suspend fun createUser(
        @Body body: CreateRequest
    ): Response<SimpleResponse>

    @GET("user/getUsername")
    suspend fun getUsername(
        @Header("Authorization") token: String,
    ): Response<GetUsernameResponse>

    @GET("user/getProfile")
    suspend fun getProfile(
        @Header("Authorization") token: String,
    ): Response<ProfileResponse>

    @GET("admin/getProfile")
    suspend fun getProfileAdmin(
        @Header("Authorization") token: String,
    ): Response<ProfileResponse>

    @PUT("user/updateProfile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body body: EditProfileRequest
    ): Response<GeneralResponse>


    @Multipart
    @POST("transaksi/createTransaksi")
    suspend fun createTransaksi(
        @Header("Authorization") token: String,
        @Part photos: List<MultipartBody.Part>,
        @Part("data") data: RequestBody
    ): Response<SimpleResponse>

    @GET("admin/getUsername")
    suspend fun getUsernameAdmin(
        @Header("Authorization") token: String
    ): Response<GetUsernameResponse>

    @GET("admin/getAllTransaksi")
    suspend fun getAllTransaksiAdmin(
        @Header("Authorization") token: String,
    ): Response<GetAllTransaksiAdminResponse>

    @GET("transaksi/getAllTransaksi")
    suspend fun getAllTransaksiUser(
        @Header("Authorization") token: String,
    ): Response<GetAllTransaksiUserResponse>

    @POST("admin/getTransaksi")
    suspend fun getTransaksi(
        @Header("Authorization") token: String,
        @Body body: TransaksiRequest
    ): Response<GetTransaksiResponse>

    @PUT("admin/updateTransaksi")
    suspend fun updateTransaksi(
        @Header("Authorization") token: String,
        @Body body: UpdateTransaksiRequest
    ): Response<GeneralResponse>

}
