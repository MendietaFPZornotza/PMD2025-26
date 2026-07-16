package com.example.fragment_1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), FragmentAdibidea.OnFragmentInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Fragmenta gehitu hasieran
        /* Horrek esan nahi du FragmentAdibidea-a
         activity_main.xml-eko FrameLayout-ean agertuko dela.*/
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorFragment, FragmentAdibidea())
            .commit()
    }
   override fun onMezuaBidali(mezua: String) {
        Toast.makeText(this, "Fragmentetik jaso dut: $mezua", Toast.LENGTH_LONG).show()
    }
}