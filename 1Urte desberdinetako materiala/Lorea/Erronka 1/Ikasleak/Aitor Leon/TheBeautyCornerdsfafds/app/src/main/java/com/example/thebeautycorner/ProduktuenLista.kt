package com.example.thebeautycorner

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thebeautycorner.adapters.ProduktuaAdapter
import com.example.thebeautycorner.model.ProdListatu

class ProduktuenLista : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.produktuenlista) // Layout fitxategia RecycleView-arekin

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val productos = produktuakDBBtikHartu()
        recyclerView.adapter = ProduktuaAdapter(productos)
    }

    // Produktuak DBB-tik hartzeko funtzioa
    private fun produktuakDBBtikHartu(): List<ProdListatu> {
        val produktuak = mutableListOf<ProdListatu>()
        val admin = AdminSQLiteOpenHelper(this, "thebeautycorner", null, 2)
        val bd = admin.readableDatabase
        val cursor = bd.rawQuery("SELECT kodea, izena, mota, marka, jatorria, prezioa, eskuragarritasuna FROM produktuak", null)

        // Buklea sortu, horrela 20 produktu badaude 20 bloke sortuko dira
        if (cursor.moveToFirst()) {
            do {
                // Kontsultaren baloreak hartu
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("kodea"))
                val izena = cursor.getString(cursor.getColumnIndexOrThrow("izena"))
                val prezioa = cursor.getDouble(cursor.getColumnIndexOrThrow("prezioa"))
                val mota = cursor.getString(cursor.getColumnIndexOrThrow("mota"))

                // Produktua listara gehitu
                produktuak.add(
                    ProdListatu(
                        id,
                        izena,
                        mota,
                        prezioa))
            } while (cursor.moveToNext())
        }
        cursor.close()
        bd.close()
        return produktuak

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return true//super.onCreateOptionsMenu(menu)

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
