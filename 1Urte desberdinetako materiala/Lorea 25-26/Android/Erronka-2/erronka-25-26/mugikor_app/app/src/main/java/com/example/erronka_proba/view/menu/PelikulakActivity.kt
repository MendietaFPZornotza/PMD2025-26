package com.example.erronka_proba.view.menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.erronka_proba.BuildConfig
import com.example.erronka_proba.R
import com.example.erronka_proba.adapter.ListAdapter
import com.example.erronka_proba.controller.menu.MenuListController
import com.example.erronka_proba.model.domain.EventCategory
import com.example.erronka_proba.model.ui.ListItem
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

/**
 * Fitxategia: PelikulakActivity.kt
 *
 * Zertarako da?
 * - Pelikulen zerrenda erakusteko pantaila (activity_list layout komuna).
 *
 * Zer egiten du?
 * - RecyclerView + ListAdapter konfiguratzen du.
 * - MenuListController-ekin karga, bilaketa eta genero filtroa kudeatzen du.
 * - Toolbar back -> finish().
 *
 * Log-ak:
 * - Lifecycle, bilaketa/filtro ekintzak eta aukeraketak jarraitzeko.
 */
class PelikulakActivity : AppCompatActivity(), MenuListController.View {

    companion object {
        private const val TAG = "PelikulakActivity"
    }

    private lateinit var controller: MenuListController
    private lateinit var adapter: ListAdapter

    private lateinit var toolbar: MaterialToolbar
    private lateinit var etSearch: TextInputEditText
    private lateinit var btnFilter: MaterialButton
    private lateinit var rv: RecyclerView

    private var filterLabel: String = "Filtroak"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate(): PelikulakActivity")

        setContentView(R.layout.activity_list)

        toolbar = findViewById(R.id.toolbar)
        etSearch = findViewById(R.id.etSearch)
        btnFilter = findViewById(R.id.btnFilter)
        rv = findViewById(R.id.rvList)

        applyInsetsToToolbar(toolbar)

        toolbar.setNavigationOnClickListener {
            Log.d(TAG, "Toolbar back -> finish()")
            finish()
        }

        adapter = ListAdapter(
            scope = lifecycleScope,
            ftps = com.example.erronka_proba.data.ftps.FtpsImageClient(
                host = BuildConfig.FTPS_HOST,
                port = BuildConfig.FTPS_PORT,
                user = BuildConfig.FTPS_USER,
                pass = BuildConfig.FTPS_PASS
            )
        ) { item ->
            Log.d(TAG, "Cart klik: id=${item.id} titleLen=${item.title.length}")
            controller.onCartClicked(item)
        }

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        controller = MenuListController(
            view = this,
            scope = lifecycleScope,
            category = EventCategory.MOVIES
        )
        controller.onCreate()

        etSearch.addTextChangedListener {
            val text = it?.toString().orEmpty()
            Log.v(TAG, "Search changed: '$text'")
            controller.onSearchChanged(text)
        }

        btnFilter.setOnClickListener {
            Log.d(TAG, "Filter klik")
            controller.onFilterClicked()
        }
    }

    private fun applyInsetsToToolbar(toolbar: MaterialToolbar) {
        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { v, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            v.updatePadding(top = top)
            insets
        }
    }

    // ---- View impl ----

    override fun showTitle(title: String) {
        Log.d(TAG, "showTitle(): $title")
        toolbar.title = title
    }

    override fun setFilterButtonText(text: String) {
        Log.d(TAG, "setFilterButtonText(): $text")
        filterLabel = text
        btnFilter.text = text
    }

    override fun submitList(list: List<ListItem>) {
        Log.i(TAG, "submitList(): size=${list.size}")
        adapter.submit(list)
    }

    override fun applyFilter(query: String, genre: String?) {
        Log.v(TAG, "applyFilter(): query='$query' genre=${genre ?: "null"}")
        adapter.filter(query, genre)
    }

    override fun openGenreMenu(options: List<String>) {
        Log.d(TAG, "openGenreMenu(): options=${options.size}")

        val popup = PopupMenu(this, btnFilter)
        options.forEachIndexed { index, s ->
            popup.menu.add(0, index, index, s)
        }

        popup.setOnMenuItemClickListener { item ->
            val picked = item.title.toString()
            Log.d(TAG, "Genre picked: '$picked'")

            controller.onGenrePicked(
                genre = picked,
                currentQuery = etSearch.text?.toString().orEmpty()
            )
            true
        }

        popup.show()
    }

    override fun goTo(intent: Intent) {
        Log.i(TAG, "goTo(): ${intent.component?.className}")
        startActivity(intent)
    }

    override fun context(): Context = this

    override fun setLoading(loading: Boolean) {
        Log.d(TAG, "setLoading(): $loading")
        btnFilter.isEnabled = !loading
        btnFilter.text = if (loading) "Kargatzen..." else filterLabel
    }

    override fun showMessage(msg: String) {
        // Originalean: /* si quieres Toast */
        Log.d(TAG, "showMessage(): (ez da erakusten) $msg")
    }
}