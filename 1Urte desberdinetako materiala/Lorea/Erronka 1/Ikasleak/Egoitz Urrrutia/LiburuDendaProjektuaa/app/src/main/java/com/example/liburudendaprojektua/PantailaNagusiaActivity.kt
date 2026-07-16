package com.example.liburudendaprojektua

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PantailaNagusiaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantaila_nagusia)
    }
    //Menua ikuskarazi
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)
        return true
    }
//Menuak egiten duena
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.liburuaSortu -> {
                Toast.makeText(this, "sortu", Toast.LENGTH_LONG).show()
                val intent = Intent(this, LiburuaGehitu::class.java)
                startActivity(intent)
                true
            }

            R.id.liburuakIkusi -> {
                val intent = Intent(this, LiburuakIkusi::class.java)
                startActivity(intent)
                true
            }

            R.id.saioaItxi -> {
                val sharedPreferences = getSharedPreferences("nombreDeSesion", MODE_PRIVATE)
                sharedPreferences.edit().clear().apply()
                finish() // Pantaila itxi
                true
            }

            R.id.Irten -> {
                finishAffinity() // Aplikazioa itxi
                true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun mostrarLiburuak() {
       val dbHelper = AdminSQLiteOpenHelper(this, "liburuDB", null, 1)

        val db = dbHelper.readableDatabase

        // SELECTA egiten du
        val cursor = db.rawQuery("SELECT izenburua, egilea, prezioa FROM liburuak", null)

        if (cursor.count > 0) {
            val libros = mutableListOf<String>()

            while (cursor.moveToNext()) {
                val izenburua = cursor.getString(0)
                val egilea = cursor.getString(1)
                val prezioa = cursor.getDouble(2)

                libros.add("$izenburua - $egilea - $prezioa €")
            }
            cursor.close()

            val intent = Intent(this, LiburuakIkusi::class.java)
            intent.putStringArrayListExtra("liburuak", ArrayList(libros))
            startActivity(intent)

        } else {
            Toast.makeText(this, "Ez dago libururik gordeta", Toast.LENGTH_SHORT).show()
            cursor.close()
        }


    }
}