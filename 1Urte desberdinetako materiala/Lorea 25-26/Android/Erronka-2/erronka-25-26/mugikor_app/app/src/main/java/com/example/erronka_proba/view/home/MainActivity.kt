package com.example.erronka_proba.view.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.erronka_proba.BuildConfig
import com.example.erronka_proba.R
import com.example.erronka_proba.adapter.HomeAdapter
import com.example.erronka_proba.controller.home.MainController
import com.example.erronka_proba.model.ui.HomeItem
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

/**
 * Fitxategia: MainActivity.kt
 *
 * Zertarako da?
 * - Aplikazioko Home pantaila nagusia: drawer menua + home zerrenda (section + event card).
 *
 * Zer egiten du?
 * - UI osagaiak lotu (drawer, navView, toolbar, botoiak, recycler, progress).
 * - HomeAdapter konfiguratzen du (SeeAll / EventClick / CartClick).
 * - MainController-i delegatzen dizkio ekintzak (drawer aukerak, profile menu, home karga).
 * - Status bar / insets doikuntzak egiten ditu.
 *
 * Nola erabiltzen da?
 * - Login ondoren normalean hemen sartzen da.
 *
 * Log-ak:
 * - Lifecycle, drawer irekiera/itxiera, profile menu aukerak, adapter callback-ak eta loading/home render.
 */
class MainActivity : AppCompatActivity(), MainController.View {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var btnMenu: ImageButton
    private lateinit var btnProfile: ImageButton
    private lateinit var rvHome: RecyclerView
    private lateinit var progress: ProgressBar

    private lateinit var adapter: HomeAdapter
    private lateinit var controller: MainController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate(): MainActivity")

        setContentView(R.layout.activity_main)

        // Sistema barra eta insets: portaera originala
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false

        // Controller (scope: lifecycleScope)
        controller = MainController(this, lifecycleScope)

        // UI osagaiak
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        applyInsetsToToolbar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        btnMenu = findViewById(R.id.btnMenu)
        btnProfile = findViewById(R.id.btnProfile)
        rvHome = findViewById(R.id.rvHome)
        progress = findViewById(R.id.progress)

        // Drawer irekitzeko/ixteko botoia
        btnMenu.setOnClickListener {
            val open = drawerLayout.isDrawerOpen(GravityCompat.START)
            Log.d(TAG, "btnMenu klik -> drawer open=$open")

            if (open) drawerLayout.closeDrawer(GravityCompat.START)
            else drawerLayout.openDrawer(GravityCompat.START)
        }

        // Profile menu (popup)
        btnProfile.setOnClickListener { anchor ->
            Log.d(TAG, "btnProfile klik -> showProfileMenu()")
            showProfileMenu(anchor)
        }

        // Drawer menuko aukerak
        navView.setNavigationItemSelectedListener { item ->
            Log.d(TAG, "Drawer item selected: itemId=${item.itemId}")
            drawerLayout.closeDrawer(GravityCompat.START)
            controller.onDrawerItemSelected(item.itemId)
            true
        }

        // Adapter: click-ak controller-era
        adapter = HomeAdapter(
            onSeeAllClick = { sectionTitle ->
                Log.d(TAG, "HomeAdapter onSeeAllClick: '$sectionTitle'")
                controller.onSeeAll(sectionTitle)
            },
            onEventClick = { card ->
                Log.d(TAG, "HomeAdapter onEventClick: id=${card.id} titleLen=${card.title.length}")
                controller.onEventClicked(card)
            },
            onCartClick = { card ->
                Log.d(TAG, "HomeAdapter onCartClick: id=${card.id} titleLen=${card.title.length}")
                controller.onCartClicked(card)
            },
            scope = lifecycleScope,
            ftps = com.example.erronka_proba.data.ftps.FtpsImageClient(
                host = BuildConfig.FTPS_HOST,
                port = BuildConfig.FTPS_PORT,
                user = BuildConfig.FTPS_USER,
                pass = BuildConfig.FTPS_PASS
            )
        )

        rvHome.layoutManager = LinearLayoutManager(this)
        rvHome.adapter = adapter

        // Home edukia kargatu
        controller.onCreate()
    }

    /**
     * Toolbar-ari status bar insets aplikatzen dizkio (portaera originala).
     */
    private fun applyInsetsToToolbar(toolbar: MaterialToolbar) {
        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { v, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            v.setPadding(v.paddingLeft, top, v.paddingRight, v.paddingBottom)
            insets
        }
    }

    /**
     * Profile menu txikia erakusten du (PopupMenu):
     * - Editatu profila
     * - Logout
     */
    private fun showProfileMenu(anchor: View) {
        val popup = PopupMenu(this, anchor)
        popup.menuInflater.inflate(R.menu.menu_profile, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit_profile -> {
                    Log.i(TAG, "Profile menu: edit_profile")
                    controller.onProfileEdit()
                    true
                }

                R.id.action_logout -> {
                    Log.i(TAG, "Profile menu: logout")
                    controller.onLogout()
                    true
                }

                else -> false
            }
        }

        popup.show()
    }

    // ---- MainController.View ----

    override fun showHome(items: List<HomeItem>) {
        Log.i(TAG, "showHome(): items=${items.size}")
        adapter.submitList(items)
    }

    override fun setLoading(loading: Boolean) {
        Log.d(TAG, "setLoading(): $loading")
        progress.visibility = if (loading) View.VISIBLE else View.GONE
    }

    override fun showMessage(msg: String) {
        Log.d(TAG, "showMessage(): $msg")
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun goTo(intent: Intent, finish: Boolean) {
        Log.i(TAG, "goTo(): ${intent.component?.className} finish=$finish")
        startActivity(intent)
        if (finish) finish()
    }

    override fun context() = this
}
