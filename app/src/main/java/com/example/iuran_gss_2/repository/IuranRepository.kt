package com.example.iuran_gss_2.repository

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.model.local.CreateRequest
import com.example.iuran_gss_2.model.local.DataHolder
import com.example.iuran_gss_2.model.local.EditProfileRequest
import com.example.iuran_gss_2.model.local.Event
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
import com.example.iuran_gss_2.network.ApiService
import kotlinx.coroutines.Dispatchers
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class IuranRepository(
    private val sharedPreferences: SharedPreferences,
    private val apiService: ApiService, private val context: Context,
) {

    fun createUser(request: CreateRequest): LiveData<Event<SimpleResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val response = apiService.createUser(request)
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Indekosku Repository", "Errornya di API")
                emit(Event.Error(200, "Email sudah terdaftar"))
            }
        }

    fun getUsernameAdmin(): LiveData<Event<GetUsernameResponse>> = liveData(Dispatchers.IO) {
        emit(Event.Loading)
        try {
            val account = getData()
            val response = apiService.getUsernameAdmin(account.token.toString())
            if (response.isSuccessful) {

                val data = response.body()
                data?.let {
                    emit(Event.Success(it))
                }
            } else {
                val error = response.errorBody()?.toString()
                if (error != null) {
                    val jsonObject = JSONObject(error)
                    val message = jsonObject.getString("message")
                    emit(Event.Error(null, message))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Indekosku Repository", "Errornya di API")
            emit(Event.Error(null, context.getString(R.string.email_already_taken)))
        }
    }

    fun getLogin(request: LoginRequest): LiveData<Event<LoginResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val response = apiService.login(request)
                if (response.isSuccessful) {

                    val data = response.body()
                    data?.let {
                        if (data.status != "400") {
                            emit(Event.Success(it))
                        } else {
                            emit(Event.Error(null, data.msg))
                        }
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Indekosku Repository", "Errornya di API")
                emit(Event.Error(null, context.getString(R.string.email_already_taken)))
            }
        }

    fun createTransaction(
        token: String,
        photos: List<MultipartBody.Part>,
        request: RequestBody
    ): LiveData<Event<SimpleResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val response = apiService.createTransaksi(token, photos, request)
                if (response.isSuccessful) {

                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Indekosku Repository", "Errornya di API")
                emit(Event.Error(null, context.getString(R.string.create_transaction_failed)))
            }
        }

    fun updateTransaksi(request: UpdateTransaksiRequest): LiveData<Event<GeneralResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val account = getData()
                val response = apiService.updateTransaksi(account.token.toString(), request)
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Indekosku Repository", "Errornya di API")
                emit(Event.Error(null, context.getString(R.string.get_transaction_failed)))
            }
        }

    fun downloadPdf(
        context: Context,
        url: String,
        fileName: String,
        callback: (Boolean, String) -> Unit
    ) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Download PDF", "Download failed: ${e.message}")
                callback(false, "Download failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("Download PDF", "Download failed: ${response.message}")
                    callback(false, "Download failed: ${response.message}")
                    return
                }

                val file =
                    File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
                response.body?.let { body ->
                    try {
                        val inputStream = body.byteStream()
                        val outputStream = FileOutputStream(file)
                        inputStream.use { input ->
                            outputStream.use { output ->
                                input.copyTo(output)
                            }
                        }
                        Log.d("Download PDF", "Downloaded to ${file.absolutePath}")
                        callback(true, file.absolutePath)
                    } catch (e: IOException) {
                        Log.e("Download PDF", "Error saving file: ${e.message}")
                        callback(false, "Error saving file: ${e.message}")
                    }
                }
            }
        })
    }

    fun getTransaksi(request: TransaksiRequest): LiveData<Event<GetTransaksiResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val account = getData()
                val response = apiService.getTransaksi(account.token.toString(), request)
                Log.d("Testing", response.toString())
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Indekosku Repository", "Errornya di API")
                emit(Event.Error(null, context.getString(R.string.get_transaction_failed)))
            }
        }


    fun getAllTransaksiAdmin(token: String): LiveData<Event<GetAllTransaksiAdminResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val response = apiService.getAllTransaksiAdmin(token)
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Indekosku Repository", "Errornya di API")
                emit(Event.Error(null, context.getString(R.string.get_transaction_failed)))
            }
        }

    fun getAllTransaksiUser(token: String): LiveData<Event<GetAllTransaksiUserResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val response = apiService.getAllTransaksiUser(token)
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Indekosku Repository", "Errornya di API")
                emit(Event.Error(null, context.getString(R.string.get_transaction_failed)))
            }
        }

    fun getUsername(token: String): LiveData<Event<GetUsernameResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val response = apiService.getUsername(token)
                Log.d("Testing", "token : $token")
                Log.d("Testing", "response : $response")
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Indekosku Repository", "Errornya di API")
                emit(Event.Error(null, context.getString(R.string.get_username_failed)))
            }
        }

    fun getProfile(token: String): LiveData<Event<ProfileResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val response = apiService.getProfile(token)
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Indekosku Repository", "Errornya di API")
                emit(Event.Error(null, context.getString(R.string.get_profile_failed)))
            }
        }

    fun getProfileAdmin(token: String): LiveData<Event<ProfileResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val response = apiService.getProfileAdmin(token)
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Indekosku Repository", "Errornya di API")
                emit(Event.Error(null, context.getString(R.string.get_profile_failed)))
            }
        }

    fun editProfile(token: String, request: EditProfileRequest): LiveData<Event<GeneralResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val response = apiService.updateProfile(token, request)
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        emit(Event.Success(it))
                    }
                } else {
                    val error = response.errorBody()?.toString()
                    if (error != null) {
                        val jsonObject = JSONObject(error)
                        val message = jsonObject.getString("message")
                        emit(Event.Error(null, message))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("Indekosku Repository", "Errornya di API")
                emit(Event.Error(null, context.getString(R.string.get_profile_failed)))
            }
        }

    fun logOut() {
        sharedPreferences.edit().putString("email", null).putString("password", null)
            .putString("token", null).putString("role", null).apply()
    }

    fun saveOnboarding(onBoarding: Boolean) {
        sharedPreferences.edit().putBoolean("onboarding_complete", onBoarding).apply()
    }

    fun saveEncryptedValues(email: String, password: String, token: String, role: String) {
        sharedPreferences.edit().putString("email", email).putString("password", password)
            .putString("token", token).putString("role", role).apply()
        getData()
    }

    fun getData(): DataHolder {
        val email = sharedPreferences.getString("email", null)
        val password = sharedPreferences.getString("password", null)
        val token = sharedPreferences.getString("token", null)
        val role = sharedPreferences.getString("role", null)
        return DataHolder(email, password, token, role)
    }

    suspend fun refreshToken() {
        val account = getData()
        val request = LoginRequest(account.email, account.password)
        val response = apiService.login(request)
        var token = "Bearer "
        if (response.isSuccessful) {
            val data = response.body()
            token += data?.token.toString()
            val role = data?.status.toString()
            saveEncryptedValues(
                account.email.toString(),
                account.password.toString(),
                token,
                role = role
            )
        }
    }

    fun getOnboarding(): Boolean =
        sharedPreferences.getBoolean("onboarding_complete", false)

    fun getRole(): String? = sharedPreferences.getString("role", null)

}