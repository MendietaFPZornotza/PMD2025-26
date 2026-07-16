package com.example.thebeautycorner

import com.example.thebeautycorner.ProduktuenLista
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Printzipala : AppCompatActivity() {

    private lateinit var txtOngiEtorri: TextView

    //LOGEATZEN EDO ERREGISTRATZEN DEN ERABILTZAILEAREN INFORMAZIOA GORDE
    private fun erabHasieratu() {
        txtOngiEtorri = findViewById(R.id.txtOngiEtorri)
        val sharedPreferences: SharedPreferences = getSharedPreferences("MiAppPrefs", MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "Usuario")
        txtOngiEtorri.text = "Ongi etorri, $email!"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.printzipala)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Korroa hartzeko funtzioa
        erabHasieratu()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return true//super.onCreateOptionsMenu(menu)

        erabHasieratu()

    }

    //Beridazten duen funtzioa gure menuaren botoiak funtzionatzeko
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuKosmetikoBerria   -> {
                val intent: Intent = Intent(this, ProduktuaGehitu::class.java)

                startActivity(intent)
                true
            }

            R.id.menuKosmetikoZerrenda -> {
                val intent: Intent = Intent(this, ProduktuenLista::class.java)
                startActivity(intent)
                true
            }

            R.id.menuSaioaItxi         -> {
                val intent: Intent = Intent(this, Login::class.java)
                startActivity(intent)
                true
            }

            R.id.menuIrten             -> {
                finishAffinity()
                true
            }

            else                       -> return super.onOptionsItemSelected(item)
        }
    }
}