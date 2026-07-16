package com.example.proiektua

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {
    lateinit var welcomeTextView: TextView
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        welcomeTextView = findViewById(R.id.text_welcome)
        drawerLayout = findViewById(R.id.drawer_layout)

        // Obtener el nombre del usuario desde el Intent
        val username = intent.getStringExtra("USERNAME") ?: "Erabiltzaile"

        // Configurar el mensaje de bienvenida con el nombre del usuario
        welcomeTextView.text = "Ongi etorri, $username!"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_add_product -> {
                // Acción para "Kirol produktu Berria Gehitu"
                val intent = Intent(this, AddProductActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_product_list -> {
                // Acción para "Kirol produktuen Zerrenda"
                val intent = Intent(this, ProductListActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.nav_logout -> {
                // "Saioa itxi" - Redirigir al login
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()  // Cierra la actividad actual
                true
            }
            R.id.nav_exit -> {
                // "Irten" - Cerrar la aplicación
                finishAffinity()  // Cierra la aplicación por completo
                true
            }
            else -> false
        }
    }
}
