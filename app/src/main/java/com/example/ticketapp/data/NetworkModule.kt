package com.example.ticketapp.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    var BASE_URL_TEST = ""

     const val BASE_URL = "https://bilet.bgsoft.uz/"

    @Provides
    fun provideRetrofitInstance(
        okhttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .client(okhttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }


    @Provides
    fun gson(): Gson {
        return GsonBuilder().create()
    }


    @Provides
    fun gsonConvertFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    fun okHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient {

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(false)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
//            .addInterceptor(PlutoInterceptor())
//            .cache(cache)
            .build()
    }

}