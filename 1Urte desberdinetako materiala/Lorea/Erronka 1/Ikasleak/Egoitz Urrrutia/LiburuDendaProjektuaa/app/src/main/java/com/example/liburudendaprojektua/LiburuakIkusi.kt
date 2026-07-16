package com.example.liburudendaprojektua


import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class LiburuakIkusi : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liburuak_ikusi)

        recyclerView = findViewById(R.id.recyclerViewLiburuak)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val liburuak = getAllLiburuak()
        recyclerView.adapter = LiburuaAdapter(liburuak)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)

        val liburuakIkusiItem = menu.findItem(R.id.liburuakIkusi)
        val shouldHideLiburuakIkusi = true

        if (shouldHideLiburuakIkusi) {
            liburuakIkusiItem.isVisible = false // Menuan ez du erakusten
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.liburuaSortu -> {
                Toast.makeText(this, "sortu", Toast.LENGTH_LONG).show()
                val intent = Intent(this, LiburuaGehitu::class.java)
                startActivity(intent)
                true
            }

            R.id.liburuakIkusi -> {

                val shouldHideLiburuakIkusi = true

                if (shouldHideLiburuakIkusi) {
                    item.isVisible = false
                    return false
                }

                val intent = Intent(this, LiburuakIkusi::class.java)
                startActivity(intent)
                true
            }

            R.id.saioaItxi -> {
                val sharedPreferences = getSharedPreferences("nombreDeSesion", MODE_PRIVATE)
                sharedPreferences.edit().clear().apply() // Limpiar preferencias
                finish() // Cierra la actividad actual
                Toast.makeText(this, "Saioa itxi da", Toast.LENGTH_LONG).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Reinicia la actividad
                startActivity(intent)
                true
            }


            R.id.Irten -> {
                finishAffinity() // Aplikazioa ixten dau
                true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun getAllLiburuak(): List<Liburua> {
        val liburuakList = mutableListOf<Liburua>()
        dbHelper = AdminSQLiteOpenHelper(this, "liburuDB", null, 2)

        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT id,izenburua,egilea,prezioa FROM liburuak", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val izenburua = cursor.getString(cursor.getColumnIndexOrThrow("izenburua"))
                val egilea = cursor.getString(cursor.getColumnIndexOrThrow("egilea"))
                val prezioa = cursor.getDouble(cursor.getColumnIndexOrThrow("prezioa"))


                liburuakList.add(
                    Liburua(
                        id,
                        izenburua,
                        egilea,
                        prezioa
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return liburuakList
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // Eguneraten du liburuen lista
            getAllLiburuak()
        }
    }



}
