package com.example.mvc_menu.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mvc_menu.R
import com.example.mvc_menu.ui.theme.Mvc_menuTheme
import androidx.appcompat.widget.Toolbar

// View: interfazea kudeatzen du, baina logika Controller-era pasatzen du.
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar toolbar como ActionBar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.gorde -> {
                Toast.makeText(this, "Gorde botoia sakatu da", Toast.LENGTH_LONG).show()
                true
            }
            R.id.ireki -> {
                Toast.makeText(this, "Ireki botoia sakatu da", Toast.LENGTH_LONG).show()
                true
            }
            R.id.irten -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}