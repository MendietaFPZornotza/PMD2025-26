package com.example.proiektu_denda

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProductManagementActivity : AppCompatActivity() {
    private lateinit var izenaproduktua:EditText
    private lateinit var spinnermota:Spinner
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
    private lateinit var gehitu: Button
    private lateinit var eskuragarria: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_management)
        izenaproduktua = findViewById(R.id.izenaproduktuaa)
        spinnermota = findViewById(R.id.spinnerMota)
        jatorria = findViewById(R.id.jatorria)
        checkboxolioa = findViewById(R.id.checkboxOlioa)
        checkboxgatza = findViewById(R.id.checkboxGatza)
        checkboxarrautza = findViewById(R.id.checboxArrautzak)
        checkboxtomatea = findViewById(R.id.checkboxTomatea)
        checkboxgurina = findViewById(R.id.checkBoxGurina)
        checkboxberakatza = findViewById(R.id.checkBoxBerakatza)
        precio = findViewById(R.id.prezioa)
        checkboxbai = findViewById(R.id.checkBoxEskuragarri)
        checkboxez = findViewById(R.id.checkBoxEzEskuragarri)
        gehitu = findViewById(R.id.gehituproduktua)

        //spinner janari motena
        val motak = arrayOf("Haragiak", "Arrainak eta itsaskiak", "Esnekiak", "Barazkiak eta frutak", "Zerealak eta haziak")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, motak)
        spinnermota.adapter=adapter


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

        //produktu berri bat sortzeko
        gehitu.setOnClickListener{
            val admin = AdminSQLiteOpenHelper(this, "administracion.db", null, 1) // Usa la misma versión
            val bd = admin.writableDatabase

            val izenaproduktuaa: String = izenaproduktua.getText().toString()
            val jatorriaa: String = jatorria.getText().toString()
            val precioo: String = precio.getText().toString()
            val spinnermotaa: String = findViewById<Spinner>(R.id.spinnerMota).selectedItem.toString()
            //Honek egiten du checkbox bat aukeratzean ze String gordeko den DB-ean
            //Adibidez. Gatza checkboxa aukeratzen bada, 'Gatza' hitzarekin gordeko DB-an
            val opciones = mapOf(
                checkboxgatza to "Gatza",
                checkboxolioa to "Olioa",
                checkboxberakatza to "Berakatza",
                checkboxtomatea to "Tomatea",
                checkboxarrautza to "Arrautza",
                checkboxgurina to "Gurina"
            )

            // Honek hartzen du bakarrik aukeratuta dagoen checbboxa
            val seleccionada = opciones
                .filter { (checkBox, _) -> checkBox.isChecked }
                .values
                .firstOrNull()

            if(checkboxbai.isChecked){
                eskuragarria = "Bai"
                checkboxez.isChecked = false
            } else {
                eskuragarria = "ez"
                checkboxbai.isChecked = false
            }

            //Komprobatu hutsune guztiak bete direla , checbkoxak aukeratu direla...
            if(izenaproduktuaa.isNotEmpty() && jatorriaa.isNotEmpty() && precioo.isNotEmpty()){
                if(checkboxgatza.isChecked || checkboxolioa.isChecked || checkboxberakatza.isChecked || checkboxgurina.isChecked || checkboxarrautza.isChecked || checkboxtomatea.isChecked){
                    if(checkboxbai.isChecked || checkboxez.isChecked){
                        val registro = ContentValues()
                        registro.put("izena", izenaproduktuaa)
                        registro.put("mota", spinnermotaa)
                        registro.put("jatorria", jatorriaa)
                        registro.put("osagarria", seleccionada)
                        registro.put("prezioa", precioo)
                        registro.put("eskuragarritasuna", eskuragarria)

                        bd.insert("productos", null, registro)

                        bd.close()
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

                        Toast.makeText(this, "Gehitu da", Toast.LENGTH_LONG).show()
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