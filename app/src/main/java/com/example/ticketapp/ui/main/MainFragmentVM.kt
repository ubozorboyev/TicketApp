package com.example.ticketapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketapp.data.NetworkModule.BASE_URL
import com.example.ticketapp.data.NetworkModule.BASE_URL_TEST
import com.example.ticketapp.data.modul.QrResponse
import com.example.ticketapp.data.remote.ApiService
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainFragmentVM @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _qrResult = MutableLiveData<QrResponse>()
    val qrResult: LiveData<QrResponse> get() = _qrResult


    fun fetchDataFromUrl(path: String) {

        viewModelScope.launch {

            try {

                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

                val client = OkHttpClient().newBuilder()
                    .addInterceptor(interceptor)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build()

                val request = Request.Builder()
                    .url(BASE_URL_TEST + path)
                    .get()
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {

                    }

                    override fun onResponse(call: Call, response: Response) {

                        try {
                            _qrResult.postValue(
                                Gson().fromJson(
                                    response.body?.string(),
                                    QrResponse::class.java
                                )
                            )
                        } catch (e: Exception) {

                        }


                    }

                })
            } catch (e: Exception) {

                e.printStackTrace()
            }

        }


    }


}