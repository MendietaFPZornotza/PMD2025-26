package com.example.lorategi_proiektua

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddProductActivity : AppCompatActivity() {
    lateinit var inputName: EditText
    lateinit var inputType: Spinner
    lateinit var inputOrigin: RadioGroup
    lateinit var inputColor: EditText
    lateinit var inputPrice: EditText
    lateinit var inputAvaliable: CheckBox
    lateinit var btnGorde: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // Menuaren izenburua aldatu
        supportActionBar?.title = "Produktu Berria"

        inputName = findViewById(R.id.inputName)
        inputType = findViewById(R.id.inputType)
        inputOrigin = findViewById(R.id.inputOrigin)
        inputColor = findViewById(R.id.inputColor)
        inputPrice = findViewById(R.id.inputPrice)
        inputAvaliable = findViewById(R.id.inputAvaliable)
        btnGorde = findViewById(R.id.btnGorde)

        //SPINNERREAN AUKERAK SARTU
        val motak = arrayOf("Landarea", "Tresna", "Loreontzia")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, motak)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner: Spinner = findViewById(R.id.inputType)
        spinner.adapter = adapter

        //PRODUKTUA GEHITU DATU BASEAN
        btnGorde.setOnClickListener(){
            val name = inputName.text.toString()
            val type = inputType.selectedItem.toString()
            var origin = ""
            when (inputOrigin.checkedRadioButtonId) {
                R.id.rbSpain -> origin = "Spain"
                R.id.rbTaiwan -> origin = "Taiwan"
                R.id.rbEngland -> origin = "England"
            }
            System.out.println("Origin: $origin")
            val color = inputColor.text.toString()
            val priceString = inputPrice.text.toString()
            val price = if (priceString.isNotEmpty()) priceString.toDouble() else 0.0
            val avaliable = inputAvaliable.isChecked

            //KONPROBTAU DATUAK EGOKIAK DIRELA
            if (name.isEmpty() || color.isEmpty() || price == 0.0) {
                if (price == 0.0) {
                    Toast.makeText(this, "Prezioa ezin da 0 izan", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Datu guztiak sartu", Toast.LENGTH_SHORT).show()
                }
                return@setOnClickListener
            }
            else{
                val admin = AdminSQLiteOpenHelper(
                    this,
                    "administracion", null, 5
                )
                val bd = admin.writableDatabase
                val values = ContentValues()

                values.put("name", name)
                values.put("type", type)
                values.put("origin", origin)
                values.put("color", color)
                values.put("price", price)
                values.put("avaliable", avaliable)
                bd.insert("products", null, values)
                bd.close()
                System.out.println("Produktua gehitu da")
                Toast.makeText(this, "Produktua gehitu da", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ProductActivity::class.java)
                startActivity(intent)
                finish()
            }
        }



        //FUNCION PARA BORRAR LOS PRODUCTOS DE LA BASE DE DATOS
        fun deleteProducts(){
            val admin = AdminSQLiteOpenHelper(
                this,
                "administracion", null, 5
            )
            val bd = admin.writableDatabase
            bd.delete("products", null, null)
            bd.close()
            System.out.println("Produktu guztiak ezabatu dira")
            Toast.makeText(this, "Produktu guztiak ezabatu dira", Toast.LENGTH_SHORT).show()
        }
        //deleteProducts()
    }

    //MENUA KARGATU
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.Gehitu->{
                System.out.println("Gehitu sakatu da")
                true
            }
            R.id.Zerrenda->{
                Toast.makeText(this, "Zerrenda sakatu da", Toast.LENGTH_LONG).show()
                System.out.println("Zerrenda sakatu da")
                val intent = Intent(this, ProductActivity::class.java)
                startActivity(intent)
                finish()
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