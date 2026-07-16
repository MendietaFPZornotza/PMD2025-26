package com.example.proiektu_denda

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.sql.RowId

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var izenaproduktua: EditText
    private lateinit var spinnermota: Spinner
    private lateinit var jatorria: EditText
    private lateinit var checkboxolioa: CheckBox
    private lateinit var checkboxarrautza: CheckBox
    private lateinit var checkboxgatza: CheckBox
    private lateinit var checkboxberakatza: CheckBox
    private lateinit var checkboxtomatea: CheckBox
    private lateinit var checkboxgurina: CheckBox
    private lateinit var precio: EditText
    private lateinit var checkboxbai: CheckBox
    private lateinit var checkboxez: CheckBox
    private lateinit var aldatu: Button
    private lateinit var ezabatu: Button
    private lateinit var irten: Button
    private lateinit var eskuragarria: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        izenaproduktua = findViewById(R.id.izenaproduktuaa2)
        spinnermota = findViewById(R.id.spinnerMota2)
        jatorria = findViewById(R.id.jatorria2)
        checkboxolioa = findViewById(R.id.checkboxOlioa2)
        checkboxgatza = findViewById(R.id.checkboxGatza2)
        checkboxarrautza = findViewById(R.id.checboxArrautzak2)
        checkboxtomatea = findViewById(R.id.checkboxTomatea2)
        checkboxgurina = findViewById(R.id.checkboxGurina2)
        checkboxberakatza = findViewById(R.id.checkboxBerakatza2)
        precio = findViewById(R.id.prezioa2)
        checkboxbai = findViewById(R.id.checkBoxEskuragarri2)
        checkboxez = findViewById(R.id.checkBoxEzEskuragarri2)
        aldatu = findViewById(R.id.Aldatu)
        ezabatu = findViewById(R.id.Ezabatu)
        irten = findViewById(R.id.Irten)

        //Hartzen ditut aukeratu ditudan produktuaren datuak
        val productId = intent.getIntExtra("productId", -1)
        val productName = intent.getStringExtra("productName")
        val productType = intent.getStringExtra("productType")
        val productPrice = intent.getDoubleExtra("productPrice", 0.0)  // Aquí puedes usar `Double` para el precio
        val productOrigin = intent.getStringExtra("productOrigin")
        val productIngredients = intent.getStringExtra("productIngredients")
        val productAvailability = intent.getStringExtra("productAvailability")

        //jartzen ditut hutsune bakoitzean aukeratutako produktuaren datuak
        izenaproduktua.setText(productName)
        jatorria.setText(productOrigin)
        precio.setText("${productPrice}")

        val motak = arrayOf("Haragiak", "Arrainak eta itsaskiak", "Esnekiak", "Barazkiak eta frutak", "Zerealak eta haziak")

        //spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, motak)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnermota.adapter = adapter

        //Honek jarriko defektuz spinnerrean produktuen den janari mota
        val defaultIndex = motak.indexOf(productType)
        spinnermota.setSelection(defaultIndex)

        //checkbox denez eta bakarrik aukera bat egon behar denez, bakarrik bat aukera ahal izateko da
        checkboxarrautza.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkboxtomatea.isChecked = false
                checkboxgurina.isChecked = false
                checkboxolioa.isChecked = false
                checkboxgatza.isChecked = false
                checkboxberakatza.isChecked = false
            }
        }

        checkboxtomatea.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkboxberakatza.isChecked = false
                checkboxgurina.isChecked = false
                checkboxolioa.isChecked = false
                checkboxgatza.isChecked = false
                checkboxarrautza.isChecked = false
            }
        }

        checkboxgurina.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkboxtomatea.isChecked = false
                checkboxberakatza.isChecked = false
                checkboxolioa.isChecked = false
                checkboxgatza.isChecked = false
                checkboxarrautza.isChecked = false
            }
        }

        checkboxberakatza.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkboxtomatea.isChecked = false
                checkboxgurina.isChecked = false
                checkboxolioa.isChecked = false
                checkboxgatza.isChecked = false
                checkboxarrautza.isChecked = false
            }
        }

        checkboxolioa.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkboxtomatea.isChecked = false
                checkboxgurina.isChecked = false
                checkboxberakatza.isChecked = false
                checkboxgatza.isChecked = false
                checkboxarrautza.isChecked = false
            }
        }

        checkboxgatza.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkboxtomatea.isChecked = false
                checkboxgurina.isChecked = false
                checkboxolioa.isChecked = false
                checkboxberakatza.isChecked = false
                checkboxarrautza.isChecked = false
            }
        }

        //berdina hemen eskuragarritasunarekin
        checkboxbai.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkboxez.isChecked = false
            }
        }

        checkboxez.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkboxbai.isChecked = false
            }
        }

        //checkboxak ikusten ditu zein dituen checked aukeratu ditudan produktuak, eta checked jartzen ditu hemen
        checkboxgurina.isChecked = productIngredients?.contains("Gurina") == true
        checkboxtomatea.isChecked = productIngredients?.contains("Tomatea") == true
        checkboxgatza.isChecked = productIngredients?.contains("Gatza") == true
        checkboxberakatza.isChecked = productIngredients?.contains("Berakatza") == true
        checkboxolioa.isChecked = productIngredients?.contains("Olioa") == true
        checkboxarrautza.isChecked = productIngredients?.contains("Arrautza") == true
        checkboxbai.isChecked = productAvailability == "Bai"
        checkboxez.isChecked = productAvailability == "Ez"


        //aplikaziotik irtetzeko
        irten.setOnClickListener {
            finishAffinity()
        }

        //Datuak aldatzeko
        aldatu.setOnClickListener {
            val admin = AdminSQLiteOpenHelper(this, "administracion.db", null, 1)
            val bd = admin.writableDatabase

            val izenaproduktuaa = izenaproduktua.text.toString()
            val jatorriaa = jatorria.text.toString()
            val precioo = precio.text.toString()
            val spinnermotaa = spinnermota.selectedItem.toString()

            val opciones = mapOf(
                checkboxgatza to "Gatza",
                checkboxolioa to "Olioa",
                checkboxberakatza to "Berakatza",
                checkboxtomatea to "Tomatea",
                checkboxarrautza to "Arrautza",
                checkboxgurina to "Gurina"
            )
            val seleccionadas = opciones
                .filter { (checkBox, _) -> checkBox.isChecked }
                .values
                .joinToString(", ")

            eskuragarria = if (checkboxbai.isChecked) "Bai" else "Ez"

            //Ikusten du dena hutsik ez dagoela eta aukeratu dela checkboxak...
            if(izenaproduktuaa.isNotEmpty() && jatorriaa.isNotEmpty() && precioo.isNotEmpty()){
                if(checkboxgatza.isChecked || checkboxolioa.isChecked || checkboxberakatza.isChecked || checkboxgurina.isChecked || checkboxarrautza.isChecked || checkboxtomatea.isChecked){
                    if(checkboxbai.isChecked || checkboxez.isChecked){
                        val registro = ContentValues().apply {
                            put("izena", izenaproduktuaa)
                            put("mota", spinnermotaa)
                            put("jatorria", jatorriaa)
                            put("osagarria", seleccionadas)
                            put("prezioa", precioo)
                            put("eskuragarritasuna", eskuragarria)
                        }

                        val rowsUpdated = bd.update(
                            "productos",
                            registro,
                            "id = ?",
                            arrayOf(productId.toString())
                        )

                        if (rowsUpdated > 0) {
                            Toast.makeText(this, "Produktua aldatu da", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Errorea produktua aldatzean", Toast.LENGTH_SHORT).show()
                        }

                        bd.close()

                        // Limpia los campos después de la actualización
                        izenaproduktua.setText("")
                        jatorria.setText("")
                        precio.setText("")
                        checkboxgatza.isChecked = false
                        checkboxolioa.isChecked = false
                        checkboxberakatza.isChecked = false
                        checkboxtomatea.isChecked = false
                        checkboxgurina.isChecked = false
                        checkboxarrautza.isChecked = false
                        checkboxez.isChecked = false
                        checkboxbai.isChecked = false

                        val i = Intent(this, com.example.proiektu_denda.ProductListActivity::class.java)
                        startActivity(i)
                    }else{
                        Toast.makeText(this, "Aukeratu eskuragarritasuna", Toast.LENGTH_LONG).show()
                    }
                } else{
                    Toast.makeText(this, "Aukeratu osagarri nagusi bat", Toast.LENGTH_LONG).show()
                }
            } else{
                Toast.makeText(this, "Bete hutsune guztiak", Toast.LENGTH_LONG).show()
            }

        }

        //Aukeratutako prdouktua ezabatu egiten da
        ezabatu.setOnClickListener {
            alertaEzabatuBainoLehen(arrayOf(productId.toString()))
            //DB-ra deitu

        }
    }

    private fun alertaEzabatuBainoLehen(array: Array<String>) {
        AlertDialog.Builder(this)
            .setTitle("Kontuz!")
            .setMessage("Ziur zaude produktua ezabatu nahi duzula?")
            .setPositiveButton("Bai") { dialog, _ ->
                //Bai klikatu ostean
                val admin = AdminSQLiteOpenHelper(this, "administracion.db", null, 1)
                val bd = admin.writableDatabase

                //Produktua ezabatu
                val rowsDeleted = bd.delete("productos", "id = ?", array)

                if (rowsDeleted > 0) {
                    Toast.makeText(this, "Produktua ezabatu da.", Toast.LENGTH_SHORT).show()
                }

                bd.close()
                val i = Intent(this, com.example.proiektu_denda.ProductListActivity::class.java)
                startActivity(i)
                dialog.dismiss()

            }
            .setNegativeButton("Ez"){ dialog, _ ->

                dialog.dismiss()
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.aukerenmenua, menu)
        return true
    }

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