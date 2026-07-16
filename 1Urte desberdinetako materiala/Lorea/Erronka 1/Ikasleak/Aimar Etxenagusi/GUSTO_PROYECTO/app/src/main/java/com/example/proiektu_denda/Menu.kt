package com.example.proiektu_denda

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import java.lang.System.exit

class Menu : AppCompatActivity() {
    private lateinit var texto: TextView
    private lateinit var texto2: TextView
    private lateinit var texto3: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        texto = findViewById(R.id.texto)
        texto2 = findViewById(R.id.texto2)
        texto3 = findViewById(R.id.textView14)


        val username = intent.getStringExtra("erabiltzailea")

        //Logineko orrialdetik jaso
        val mensaje = Html.fromHtml(
            "<b><font color='#FFFFFF' size='22'>Ongi etorri, $username!</font></b><br>" +
                    "<font color='#002F5A' size='20'>GUSTO-ra!</font>"
        )
        texto.text = mensaje


        val admin = AdminSQLiteOpenHelper(this, "administracion.db", null, 1)
        val bd = admin.writableDatabase

        //Selecta produktu merkeena, eskuragarri daudenetatik
        val cursor = bd.rawQuery("SELECT izena, prezioa FROM productos WHERE eskuragarritasuna = 'Bai' ORDER BY prezioa ASC LIMIT 1", null)

        if (cursor.moveToFirst()) {

            val izena = cursor.getString(cursor.getColumnIndexOrThrow("izena"))
            val prezioa = cursor.getDouble(cursor.getColumnIndexOrThrow("prezioa"))

            val mensajeOferta = Html.fromHtml(
                "<b><font color='#2F4F4F' size='21'>✨ GUSTO-ko eskaintza onena aurkitu! ✨</font></b><br>" +  // Gris oscuro
                        "<font color='#F5F5DC' size='25'>Hure produkturik merkeena zain daukazu.</font><br>" +  // Beige suave
                        "<b><font color='#2F4F4F' size='21'>Ez galdu aukera!</font></b>"  // Gris oscuro
            )
            texto3.text = mensajeOferta


            //Hemen jartzen dut produktu merkeena bere izena eta prezioarekin
            val productoInfo = Html.fromHtml(
                "<b><font color='#002F5A' size='20'>$izena</font></b> " +
                        "<font color='#FFFFFF' size='20'>→</font> " +
                        "<b><font color='#FF4500' size='20'>$prezioa €</font></b>"
            )
            texto2.text = productoInfo
        }

        cursor.close()
    }

    //menua
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.aukerenmenua, menu)
        return true
    }

    //menuko itemen aukerak
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.produktuBerria->{
                val i = Intent(this, com.example.proiektu_denda.ProductManagementActivity::class.java)
                startActivity(i)
                true
            }

            R.id.zerrendaIkusi->{
                val i = Intent(this, com.example.proiektu_denda.ProductListActivity::class.java)
                startActivity(i)
                true
            }

            R.id.SaioaItxi->{
                Toast.makeText(this, "Saioa itxita", Toast.LENGTH_LONG).show()
                val i = Intent(this, com.example.proiektu_denda.Login::class.java)
                startActivity(i)
                true
            }

            R.id.salir->{
                finishAffinity()
                true
            }
            else->return super.onOptionsItemSelected(item)
        }
    }
}