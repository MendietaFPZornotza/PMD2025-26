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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProductDetailActivity : AppCompatActivity() {
    lateinit var editIzena: EditText
    lateinit var editMota: Spinner
    lateinit var editJatorria: RadioGroup
    lateinit var editKolorea: EditText
    lateinit var editPrezioa: EditText
    lateinit var editStock: CheckBox
    lateinit var editGorde: Button
    lateinit var editEzabatu: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // Menuaren izenburua aldatu
        supportActionBar?.title = "Produktuaren Xehetasunak"

        editMota = findViewById(R.id.inputEditType)
        //meter en el spinner los tipos de productos
        val motak = arrayOf("Landarea", "Tresna", "Loreontzia")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, motak)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner: Spinner = findViewById(R.id.inputEditType)
        spinner.adapter = adapter

        //Coger datos de la actividad anterior
        val kod = intent.getIntExtra("kod", 0)
        val izena = intent.getStringExtra("izena")
        val mota = intent.getStringExtra("mota")
        val jatorria = intent.getStringExtra("jatorria")
        val kolorea = intent.getStringExtra("kolorea")
        val prezioa = intent.getDoubleExtra("prezioa", 0.0)
        val stock = intent.getIntExtra("stock", 0)
        System.out.println("ID: $kod")
        System.out.println("Izena: $izena")
        System.out.println("Mota: $mota")
        System.out.println("Jatorria: $jatorria")
        System.out.println("Kolorea: $kolorea")
        System.out.println("Prezioa: $prezioa")
        System.out.println("Stock: $stock")

        //Mostrar los datos en la actividad
        editIzena = findViewById(R.id.inputEditName)
        editJatorria = findViewById(R.id.inputEditOrigin)
        editKolorea = findViewById(R.id.inputEditColor)
        editPrezioa = findViewById(R.id.inputEditPrice)
        editStock = findViewById(R.id.inputEditAvaliable)
        editGorde = findViewById(R.id.btnEdit)
        editEzabatu = findViewById(R.id.btnDelete)

        //Mostrar los datos en la actividad
        editIzena.setText(izena)
        editMota.setSelection(when (mota) {
            "Landarea" -> 0
            "Tresna" -> 1
            "Loreontzia" -> 2
            else -> 0
        })
        editJatorria.check(when (jatorria) {
            "Spain" -> R.id.rbSpain
            "Taiwan" -> R.id.rbTaiwan
            "England" -> R.id.rbEngland
            else -> R.id.rbSpain
        })
        editKolorea.setText(kolorea)
        editPrezioa.setText(prezioa.toString())
        if (stock == 1) {
            editStock.isChecked = true
        } else {
            editStock.isChecked = false
        }

        //DATU BASEA EGUNERATU
        editGorde.setOnClickListener(){
            val name = editIzena.text.toString()
            val type = editMota.selectedItem.toString()
            var origin = ""
            when (editJatorria.checkedRadioButtonId) {
                R.id.rbSpain -> origin = "Spain"
                R.id.rbTaiwan -> origin = "Taiwan"
                R.id.rbEngland -> origin = "England"
            }
            val color = editKolorea.text.toString()
            val priceString = editPrezioa.text.toString()
            val price = if (priceString.isNotEmpty()) priceString.toDouble() else 0.0
            val avaliable = editStock.isChecked

            //COMPROBAR QUE LOS DATOS SON CORRECTOS
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
                //DATUAK EGOKI SARTU BADIRA
                AlertDialog.Builder(this).apply {
                    setTitle("Eguneraketa baieztatu")
                    setMessage("Zihur zaude produktu hau eguneratu nahi duzula?")
                    setPositiveButton("Bai") { _, _ ->
                        val admin = AdminSQLiteOpenHelper(
                            this@ProductDetailActivity,
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
                        bd.update("products", values, "product_id=$kod", null)
                        bd.close()
                        System.out.println("Produktua eguneratu da")
                        Toast.makeText(this@ProductDetailActivity, "Produktua eguneratu da", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@ProductDetailActivity, ProductActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    setNegativeButton("Ez", null)
                }.show()
            }
        }

        //PRODUKTUA EZABATU
        editEzabatu.setOnClickListener(){
            AlertDialog.Builder(this).apply {
                setTitle("Ezabaketa baieztatu")
                setMessage("Zihur zaude produktu hau ezabatu nahi duzula? Akzio hau ezin da desegin")
                setPositiveButton("Bai") { _, _ ->
                    val admin = AdminSQLiteOpenHelper(
                        this@ProductDetailActivity,
                        "administracion", null, 5
                    )
                    val bd = admin.writableDatabase
                    bd.delete("products", "product_id=$kod", null)
                    bd.close()
                    System.out.println("Produktua ezabatu da")
                    Toast.makeText(this@ProductDetailActivity, "Produktua ezabatu da", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ProductDetailActivity, ProductActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                setNegativeButton("Ez", null)
            }.show()
        }

    }

    //MENUA GEHITU
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
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