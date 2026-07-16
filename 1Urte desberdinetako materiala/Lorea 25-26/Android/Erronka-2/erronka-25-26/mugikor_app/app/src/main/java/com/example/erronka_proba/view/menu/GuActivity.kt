package com.example.erronka_proba.view.menu

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.erronka_proba.R
import com.google.android.material.appbar.MaterialToolbar

/**
 * Fitxategia: GuActivity.kt
 *
 * Zertarako da?
 * - "Guri buruz" (About) pantaila erakusteko.
 *
 * Zer egiten du?
 * - activity_about layout-a kargatzen du.
 * - Toolbar-ari insets aplikatzen dizkio.
 * - Toolbar back -> finish().
 *
 * Log-ak:
 * - Lifecycle eta atzera ekintza ikusteko.
 */
class GuActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "GuActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate(): GuActivity")

        setContentView(R.layout.activity_about)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        applyInsetsToToolbar(toolbar)

        toolbar.setNavigationOnClickListener {
            Log.d(TAG, "Toolbar back -> finish()")
            finish()
        }
    }

    /**
     * Status bar insets toolbar-era (portaera originala).
     */
    private fun applyInsetsToToolbar(toolbar: MaterialToolbar) {
        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { v, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            v.updatePadding(top = top)
            insets
        }
    }
}