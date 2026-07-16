package com.example.praktika07datuakgordedb

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    //Definizioak
    lateinit var kodigoa: EditText
    lateinit var Deskrib: EditText
    lateinit var Prezioa: EditText
    lateinit var Gorde: Button
    lateinit var KontsultaK: Button
    lateinit var KontsultaD: Button
    lateinit var Ezabatu: Button
    lateinit var Aldatu: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Hasieraketa
        kodigoa=findViewById(R.id.txtKodigoa)
        Deskrib=findViewById(R.id.txtDeskribapena)
        Prezioa=findViewById(R.id.txtPrezioa)
        Gorde=findViewById(R.id.btnGorde)
        KontsultaK=findViewById(R.id.btnKontsultaK)
        KontsultaD=findViewById(R.id.btnKontsultaD)
        Ezabatu=findViewById(R.id.btnEzabatu)
        Aldatu=findViewById(R.id.btnAldatu)
        //Botoiak onClick
        Gorde.setOnClickListener {
            Create()
        }
        KontsultaK.setOnClickListener {
            ReadKodigoa()
        }
        KontsultaD.setOnClickListener {
            ReadDeskribapena()
        }
        Ezabatu.setOnClickListener {
            Ezabatu()
        }
        Aldatu.setOnClickListener {
            Aldatu()
        }
    }
    fun Create() {
        val admin = AdminSQLiteOpenHelper(
            this,
            "administracion", null, 1
        )
        val bd = admin.writableDatabase
        val kod: String = kodigoa.getText().toString()
        val deskri: String = Deskrib.getText().toString()
        val pre: String = Prezioa.getText().toString()
        val registro = ContentValues()
        registro.put("codigo", kod)
        registro.put("descripcion", deskri)
        registro.put("precio", pre)
        bd.insert("articulos", null, registro)
        bd.close()
        kodigoa.setText("")
        Deskrib.setText("")
        Prezioa.setText("")
        Toast.makeText(this, "Artikuluaren datuak kargatu dira",Toast.LENGTH_LONG).show()
    }
    fun ReadKodigoa() {
        val admin = AdminSQLiteOpenHelper(
            this,
            "administracion", null, 1
        )
        val bd = admin.writableDatabase
        val Kod: String = kodigoa.getText().toString()
        val lerroa = bd.rawQuery(
            "select descripcion,precio from articulos where codigo=$Kod", null
        )
        if (lerroa.moveToFirst()) {
            Deskrib.setText(lerroa.getString(0))
            Prezioa.setText(lerroa.getString(1))
        } else Toast.makeText(this, "Ez dago kode hori duen artikulurik", Toast.LENGTH_LONG).show()
        bd.close()
    }
    fun ReadDeskribapena() {
        val admin = AdminSQLiteOpenHelper(
            this,
            "administracion", null, 1
        )
        val bd = admin.writableDatabase
        val deskri: String = Deskrib.getText().toString()
        val lerroa = bd.rawQuery(
            "select codigo,precio from articulos where descripcion='$deskri'", null
        )
        if (lerroa.moveToFirst()) {
            kodigoa.setText(lerroa.getString(0))
            Prezioa.setText(lerroa.getString(1))
        } else Toast.makeText(this, "No existe un artículo con dicha descripción", Toast.LENGTH_LONG).show()
        bd.close()
    }
    fun Ezabatu() {
        val admin = AdminSQLiteOpenHelper(
            this,
            "administracion", null, 1
        )
        val bd = admin.writableDatabase
        val cod: String = kodigoa.getText().toString()
        val cant = bd.delete("articulos", "codigo=$cod", null)
        bd.close()
        kodigoa.setText("")
        Deskrib.setText("")
        Prezioa.setText("")
        if (cant == 1) Toast.makeText(this, "Artikulua  ezabatu da", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "Ez dago kode hori duen artikulurik", Toast.LENGTH_LONG).show()
    }
    fun Aldatu() {
        val admin = AdminSQLiteOpenHelper(
            this,
            "administracion", null, 1
        )
        val bd = admin.writableDatabase
        val kod: String = kodigoa.getText().toString()
        val deskri: String = Deskrib.getText().toString()
        val pre: String = Prezioa.getText().toString()
        val registro = ContentValues()
        registro.put("codigo", kod)
        registro.put("descripcion", deskri)
        registro.put("precio", pre)
        val kopurua = bd.update("articulos", registro, "codigo=$kod", null)
        bd.close()
        if (kopurua == 1) Toast.makeText(this, "Datuak aldatu dira.", Toast.LENGTH_SHORT).show()
        else Toast.makeText(this, "Ez dago kode hori duen artikulurik", Toast.LENGTH_LONG).show()
    }
}