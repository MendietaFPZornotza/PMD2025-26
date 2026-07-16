package com.example.mvc_menu.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
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
import com.example.mvc_menu.controller.MenuController
import com.example.mvc_menu.ui.theme.Mvc_menuTheme
import com.example.mvc_menu.R
/**
 * View: interfazea kudeatzen du. Ez du zuzen MenuModel-era egiten erreferentziarik;
 * controller-ari dei egiten dio menuaren betetzea eta menu-ekintzak kudeatzeko.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var controller: MenuController
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controller = MenuController(this)
        textView = findViewById(R.id.textView)

        // Hasierako mezua
        textView.text = "Hemen zure mezua agertuko da."

        // 🔑 Registrar Toolbar como ActionBar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }
    // Menua sortu eta Controller-i esleitu (Controller-ek sortzen du menuaren edukia)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            controller.populateMenu(menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    // Menu aukeraketa: View-k controller.handleMenuAction dei egiten du,
    // eta aldi berean onClear eta onExit callback-ak pasatzen ditu (View-rentzako ekintzak)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        controller.handleMenuAction(
            item.itemId,
            onClear = {
                // Testua garbitu; hau View-ren eremua da
                textView.text = ""
            },
            onExit = {
                // Aplikazioa itxi (aktibitate guztiak itxi)
                finishAffinity()
            }
        )

        return super.onOptionsItemSelected(item)
    }
}