package com.example.lorategi_proiektua

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductActivity : AppCompatActivity() {
    private lateinit var productList: RecyclerView
    var produktuLista: ArrayList<Lorea> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        // Menuaren izenburua aldatu
        supportActionBar?.title = "Produktu Lista"

        initRecyclerView()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    //coger productos de la base de datos y mostrarlos en el recyclerview

    fun initRecyclerView(){
        productList= findViewById(R.id.productList)
        val admin = AdminSQLiteOpenHelper(this, "administracion", null, 5)
        val bd = admin.readableDatabase
        val lerroa = bd.rawQuery("SELECT * FROM products", null)
        if (lerroa.moveToFirst()) {
            do {
                val kod = lerroa.getInt(0)
                System.out.println("DB ID: $kod")
                val name = lerroa.getString(1)
                val type = lerroa.getString(2)
                val origin = lerroa.getString(3)
                val color = lerroa.getString(4)
                val price = lerroa.getDouble(5)
                val avaliable = lerroa.getInt(6)
                val lorea = Lorea(kod, name, type, origin, color, price, avaliable)
                produktuLista.add(lorea)
            } while (lerroa.moveToNext())
        }
        bd.close()
        productList.layoutManager = LinearLayoutManager(this)
        productList.adapter = ItemAdapter(produktuLista)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.Gehitu->{
                Toast.makeText(this, "Gehitu sakatu da", Toast.LENGTH_LONG).show()
                System.out.println("Gehitu sakatu da")
                val intent = Intent(this, AddProductActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.Zerrenda->{
                System.out.println("Zerrenda sakatu da")
                true
            }
            R.id.Logout->{
                Toast.makeText(this, "Saioa itxi duzu", Toast.LENGTH_LONG).show()
                System.out.println("Logout sakatu da")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.Irten->{
                Toast.makeText(this, "Irten sakatu da", Toast.LENGTH_LONG).show()
                System.out.println("Irten sakatu da")
                finish()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}