package com.example.praktika05_activityparametroakaz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    lateinit var Botoia: Button
    lateinit var Helbidea: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        Botoia=findViewById(R.id.btnIkusi)
        Helbidea=findViewById(R.id.txtHelbidea)

        Botoia.setOnClickListener(View.OnClickListener {
            val i = Intent(this, Webgunea::class.java)
            i.putExtra("helbidea", Helbidea.getText().toString())
            startActivity(i)
        })

    }
}