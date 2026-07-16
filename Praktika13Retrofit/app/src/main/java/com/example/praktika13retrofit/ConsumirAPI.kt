package com.example.praktika13retrofit

import retrofit2.Call
import retrofit2.http.GET


interface ConsumirApi {
    @GET("persona")
    //Deia definitu Pertsona objetua berreskuratzeko
    fun getEkarri(): Call<Persona>
}