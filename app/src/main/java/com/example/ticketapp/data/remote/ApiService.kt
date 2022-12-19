package com.example.ticketapp.data.remote

import com.example.ticketapp.data.modul.QrResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("testapi.php")
    suspend fun getResultQrCode(@Query("qrcode") path: String): Response<QrResponse>

}