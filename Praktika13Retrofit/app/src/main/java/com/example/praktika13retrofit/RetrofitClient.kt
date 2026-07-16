package com.example.praktika13retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {


    // Configuración de Retrofit
    val retrofit = Retrofit.Builder()
        .baseUrl("http://demo2459173.mockable.io/")
        .addConverterFactory(GsonConverterFactory.create())  // Conversor de Gson
        .build()

    val consumirApi = retrofit.create(ConsumirApi::class.java)
}