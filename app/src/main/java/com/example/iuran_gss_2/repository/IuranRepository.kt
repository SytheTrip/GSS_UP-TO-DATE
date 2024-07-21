package com.example.iuran_gss_2.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.model.local.CreateRequest
import com.example.iuran_gss_2.model.local.CreateTransactionRequest
import com.example.iuran_gss_2.model.local.DataHolder
import com.example.iuran_gss_2.model.local.EditProfileRequest
import com.example.iuran_gss_2.model.local.Event
import com.example.iuran_gss_2.model.local.LoginRequest
import com.example.iuran_gss_2.model.remote.GeneralResponse
import com.example.iuran_gss_2.model.remote.GetAllTransaksiAdminResponse
import com.example.iuran_gss_2.model.remote.GetAllTransaksiUserResponse
import com.example.iuran_gss_2.model.remote.GetUsernameResponse
import com.example.iuran_gss_2.model.remote.LoginResponse
import com.example.iuran_gss_2.model.remote.ProfileResponse
import com.example.iuran_gss_2.model.remote.SimpleResponse
import com.example.iuran_gss_2.network.ApiService
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject

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

    fun createTransaction(request: CreateTransactionRequest): LiveData<Event<SimpleResponse>> =
        liveData(Dispatchers.IO) {
            emit(Event.Loading)
            try {
                val response = apiService.createTransaksi(request)
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

    fun editProfile(token : String, request : EditProfileRequest): LiveData<Event<GeneralResponse>> =
        liveData(Dispatchers.IO){
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
            .putString("token", null).apply()
    }

    fun saveOnboarding(onBoarding: Boolean) {
        sharedPreferences.edit().putBoolean("onboarding_complete", onBoarding).apply()
    }

    fun saveEncryptedValues(email: String, password: String, token: String) {
        sharedPreferences.edit().putString("email", email).putString("password", password)
            .putString("token", token).apply()
        getData()
    }

    fun getData(): DataHolder {
        val email = sharedPreferences.getString("email", null)
        val password = sharedPreferences.getString("password", null)
        val token = sharedPreferences.getString("token", null)
        return DataHolder(email, password, token)
    }

    suspend fun refreshToken() {
        val account = getData()
        val request = LoginRequest(account.email, account.password)
        val response = apiService.login(request)
        var token = "Bearer "
        if (response.isSuccessful) {
            val data = response.body()
            token += data?.token.toString()
            saveEncryptedValues(account.email.toString(), account.password.toString(), token)
        }
    }

    fun getOnboarding(): Boolean {
        return sharedPreferences.getBoolean("onboarding_complete", false)
    }
}