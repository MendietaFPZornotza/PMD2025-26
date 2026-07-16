package com.example.praktika13retrofit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.praktika13retrofit.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.URL
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.IOException


class MainActivity : AppCompatActivity() {
   //Vistagaz vinculetako elkartzeko
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Bistagaz  vinculetako/elkartzeko eta bere eduki guztie erabili ahal izateko
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrofit aldagai bat sortu API-ko metodoari deitu ahal izateko
        val retroftiTraer = RetrofitClient.consumirApi.getEkarri()

        //Deia sortuko dugu era sincrono batean eta objetu bat definiko dugu
        //CallBack erantzuna jasoko duena
        retroftiTraer.enqueue(object : Callback<Persona>{
            override fun onResponse(call: Call<Persona>, response: Response<Persona>) {
                binding.tvIkusi.text = response.body().toString()
            }

            override fun onFailure(call: Call<Persona>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error al consultar Api Rest", Toast.LENGTH_SHORT).show()

            }
        })

    }
}