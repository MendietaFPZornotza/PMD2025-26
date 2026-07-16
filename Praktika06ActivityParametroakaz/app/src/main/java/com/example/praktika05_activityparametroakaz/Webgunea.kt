package com.example.praktika05_activityparametroakaz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Webgunea : AppCompatActivity() {
    lateinit var Bukatu:Button
    lateinit var Web:WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        setContentView(R.layout.activity_webgunea)
       /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        Bukatu=findViewById(R.id.btnBukatu)
        Web=findViewById(R.id.wvWebgunea)

        val bundle = intent.extras
        val dato = bundle!!.getString("helbidea")
       // Log.d("Oharra", "https://www.$dato")
        Web.loadUrl("https://www.$dato")

        Bukatu.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}