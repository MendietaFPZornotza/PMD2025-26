package com.example.mvc_layout.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mvc_layout.controller.UserController
import com.example.mvc_layout.ui.theme.Mvc_layoutTheme
import android.widget.EditText
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import com.example.mvc_layout.R
class MainActivity : ComponentActivity() {
    private val controller = UserController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtIzena = findViewById<EditText>(R.id.txtIzena)
        val txtAbizena = findViewById<EditText>(R.id.txtAbizena)
        val txtAdina = findViewById<EditText>(R.id.txtAdina)
        val btnGehitu = findViewById<Button>(R.id.btnGehitu)
        val btnIkusi = findViewById<Button>(R.id.btnIkusi)
        val table = findViewById<TableLayout>(R.id.tableErabiltzaileak)

        btnGehitu.setOnClickListener {
            val izena = txtIzena.text.toString()
            val abizena = txtAbizena.text.toString()
            val adina = txtAdina.text.toString().toIntOrNull() ?: 0
            controller.gehituErabiltzailea(izena, abizena, adina)
            Toast.makeText(this, "Erabiltzailea gehituta", Toast.LENGTH_SHORT).show()
            txtIzena.text.clear()
            txtAbizena.text.clear()
            txtAdina.text.clear()
        }

        btnIkusi.setOnClickListener {
            table.removeAllViews()
            for (user in controller.lortuErabiltzaileak()) {
                val row = TableRow(this)
                val info = TextView(this)
                info.text = "${user.izena} ${user.abizena} - ${user.adina} urte"
                row.addView(info)
                table.addView(row)
            }
        }
    }
}