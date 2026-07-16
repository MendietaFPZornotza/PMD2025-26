package com.example.erronka_proba.view.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.erronka_proba.R
import com.example.erronka_proba.adapter.MyTicketsAdapter
import com.example.erronka_proba.controller.profile.ProfileController
import com.example.erronka_proba.data.repo.TicketsRepository
import com.example.erronka_proba.data.tcp.ProfileTcpService
import com.example.erronka_proba.data.tcp.SessionManager
import com.example.erronka_proba.data.tcp.TCPClient
import com.example.erronka_proba.data.tcp.TicketsTcpService
import com.example.erronka_proba.model.domain.TicketGroup
import com.example.erronka_proba.view.auth.LoginActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.io.File

/**
 * Fitxategia: NireProfilaActivity.kt
 *
 * Zertarako da?
 * - Erabiltzailearen profila ikusi/eguneratu eta “Nire sarrerak” zerrenda erakusteko.
 *
 * Zer egiten du?
 * - ProfileController-en bidez profila kargatu eta editatu.
 * - TicketsRepository erabiliz erabiltzailearen ticket taldeak kargatu.
 * - Deskarga kodea eskatuz PDF-a deskargatu (TCPClient.downloadFile) eta PDF viewer-ean ireki.
 * - Profil ezabaketa (deleteProfile) egin eta Login-era bueltatu.
 *
 * Nola erabiltzen da?
 * - MainActivity-tik (profile menu) nabigatuta.
 *
 * Log-ak:
 * - Lifecycle, ticket karga, deskarga/irekiera, edit/ezabatu ekintzak jarraitzeko.
 */
class NireProfilaActivity : AppCompatActivity(), ProfileController.View {

    companion object {
        private const val TAG = "NireProfilaActivity"
        private const val CACHE_PDF_NAME = "ticket.pdf"
        private const val MIN_CODE_LEN = 4
    }

    private lateinit var controller: ProfileController
    private lateinit var toolbar: MaterialToolbar

    // Read-only
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView

    // Edit fields
    private lateinit var tilName: TextInputLayout
    private lateinit var etName: TextInputEditText
    private lateinit var tilEmail: TextInputLayout
    private lateinit var etEmail: TextInputEditText

    private lateinit var tvPassLabel: TextView
    private lateinit var tilPass: TextInputLayout
    private lateinit var etPass: TextInputEditText
    private lateinit var tilPass2: TextInputLayout
    private lateinit var etPass2: TextInputEditText

    private lateinit var btnEdit: MaterialButton
    private lateinit var btnDelete: TextView

    // Tickets
    private lateinit var rvMyTickets: RecyclerView
    private lateinit var tvTicketsEmpty: TextView
    private lateinit var ticketsAdapter: MyTicketsAdapter

    // Data helpers
    private lateinit var session: SessionManager
    private lateinit var ticketsRepo: TicketsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate(): NireProfilaActivity")

        setContentView(R.layout.activity_profile)

        // Session + repo
        session = SessionManager(this)
        ticketsRepo = TicketsRepository(TicketsTcpService())

        // Toolbar
        toolbar = findViewById(R.id.toolbar)
        applyInsetsToToolbar(toolbar)
        toolbar.setNavigationOnClickListener {
            Log.d(TAG, "Toolbar back -> finish()")
            finish()
        }

        // Read-only fields
        tvName = findViewById(R.id.tvName)
        tvEmail = findViewById(R.id.tvEmail)

        // Edit fields
        tilName = findViewById(R.id.tilName)
        etName = findViewById(R.id.etName)
        tilEmail = findViewById(R.id.tilEmail)
        etEmail = findViewById(R.id.etEmail)

        tvPassLabel = findViewById(R.id.tvPassLabel)
        tilPass = findViewById(R.id.tilPass)
        etPass = findViewById(R.id.etPass)
        tilPass2 = findViewById(R.id.tilPass2)
        etPass2 = findViewById(R.id.etPass2)

        // Buttons
        btnEdit = findViewById(R.id.btnEdit)
        btnDelete = findViewById(R.id.btnDelete)

        // Tickets UI
        rvMyTickets = findViewById(R.id.rvMyTickets)
        tvTicketsEmpty = findViewById(R.id.tvTicketsEmpty)

        // Controller (portaera originala: SessionManager berria pasatzen da)
        controller = ProfileController(
            view = this,
            scope = lifecycleScope,
            profileService = ProfileTcpService(),
            session = SessionManager(this)
        )

        // Tickets adapter: deskarga dialog -> path -> download -> open
        ticketsAdapter = MyTicketsAdapter(emptyList()) { item ->
            Log.d(TAG, "MyTicketsAdapter onDownloadClick: codeLen=${item.downloadCode.length}")
            showDownloadDialog { codeTyped ->
                Log.d(TAG, "Kodea sartutakoa: len=${codeTyped.length}")

                lifecycleScope.launch {
                    val res = ticketsRepo.downloadPdfPath(codeTyped)

                    res.fold(
                        onSuccess = { path ->
                            Log.i(TAG, "downloadPdfPath OK: pathLen=${path.length}")
                            downloadAndOpenPdf(path)
                        },
                        onFailure = { e ->
                            Log.e(TAG, "downloadPdfPath FAIL: ${e.message}", e)
                            Toast.makeText(
                                this@NireProfilaActivity,
                                "Errorea: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }

        rvMyTickets.layoutManager = LinearLayoutManager(this)
        rvMyTickets.adapter = ticketsAdapter

        // Tickets kargatu
        loadMyTickets()

        // Edit botoia
        btnEdit.setOnClickListener {
            Log.d(TAG, "btnEdit klik -> controller.onEditButtonClicked()")
            controller.onEditButtonClicked()
        }

        // Hasierako egoera
        setEditMode(false)
        setEditButtonText("Editatu")

        // Profile karga (controller-ek egiten du)
        controller.onCreate()

        // Profil ezabatu
        btnDelete.setOnClickListener {
            Log.d(TAG, "btnDelete klik -> confirm dialog")
            showDeleteDialog()
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

    /**
     * PDF-a deskargatu (TCPClient.downloadFile) eta viewer-ekin ireki.
     *
     * Portaera originala:
     * - cacheDir/ticket.pdf fitxategian idatzi
     * - FileProvider uri sortu
     * - ACTION_VIEW + GRANT_READ_URI_PERMISSION
     */
    private fun downloadAndOpenPdf(remotePath: String) {
        Log.d(TAG, "downloadAndOpenPdf(): remotePathLen=${remotePath.trim().length}")

        lifecycleScope.launch {
            val res = TCPClient().downloadFile(remotePath)

            res.fold(
                onSuccess = { bytes ->
                    Log.i(TAG, "downloadFile OK: bytes=${bytes.size}")

                    val file = File(cacheDir, CACHE_PDF_NAME)
                    file.writeBytes(bytes)

                    val uri = FileProvider.getUriForFile(
                        this@NireProfilaActivity,
                        "${packageName}.provider",
                        file
                    )

                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, "application/pdf")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }

                    Log.i(TAG, "PDF open intent -> startActivity()")
                    startActivity(intent)
                },
                onFailure = { e ->
                    Log.e(TAG, "downloadFile FAIL: ${e.message}", e)
                    Toast.makeText(
                        this@NireProfilaActivity,
                        "PDF deskargatzean errorea: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    /**
     * Deskarga kodea eskatzeko dialog-a.
     * - Kodea >= 4 bada, OK.
     * - API 31+: blur efektua (portaera originala).
     */
    private fun showDownloadDialog(onConfirm: (String) -> Unit) {
        Log.d(TAG, "showDownloadDialog()")

        val dialogView = layoutInflater.inflate(R.layout.dialog_download_code, null)
        val til = dialogView.findViewById<TextInputLayout>(R.id.tilCode)
        val et = dialogView.findViewById<TextInputEditText>(R.id.etCode)
        val btn = dialogView.findViewById<MaterialButton>(R.id.btnConfirmDownload)

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .create()

        // Blur (portaera originala)
        if (android.os.Build.VERSION.SDK_INT >= 31) {
            window.decorView.setRenderEffect(
                android.graphics.RenderEffect.createBlurEffect(
                    20f, 20f, android.graphics.Shader.TileMode.CLAMP
                )
            )
            dialog.setOnDismissListener {
                window.decorView.setRenderEffect(null)
            }
        }

        et.addTextChangedListener {
            val code = it?.toString().orEmpty().trim()
            btn.isEnabled = code.length >= MIN_CODE_LEN
            til.error = null
        }

        btn.setOnClickListener {
            val code = et.text?.toString().orEmpty().trim()
            if (code.length < MIN_CODE_LEN) {
                til.error = "Kodea laburregia"
                return@setOnClickListener
            }

            Log.d(TAG, "Download code confirmed: len=${code.length}")
            dialog.dismiss()
            onConfirm(code)
        }

        dialog.show()
    }

    /**
     * Ticketak kargatzen ditu erabiltzailearen userId-arekin.
     */
    private fun loadMyTickets() {
        val userId = session.getUserId()
        Log.d(TAG, "loadMyTickets(): userId=$userId")

        if (userId <= 0) {
            Log.w(TAG, "userId <= 0 -> ez da ticketik kargatzen")
            showTickets(emptyList())
            return
        }

        lifecycleScope.launch {
            val res = ticketsRepo.myTickets(userId)

            res.fold(
                onSuccess = { list ->
                    Log.i(TAG, "myTickets OK: size=${list.size}")
                    showTickets(list)
                },
                onFailure = { e ->
                    Log.e(TAG, "myTickets FAIL: ${e.message}", e)
                    Toast.makeText(
                        this@NireProfilaActivity,
                        "Ezin dira sarrerak kargatu: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    showTickets(emptyList())
                }
            )
        }
    }

    /**
     * Ticket zerrenda erakutsi edo “empty” mezua.
     */
    private fun showTickets(list: List<TicketGroup>) {
        val has = list.isNotEmpty()
        Log.d(TAG, "showTickets(): has=$has size=${list.size}")

        rvMyTickets.visibility = if (has) View.VISIBLE else View.GONE
        tvTicketsEmpty.visibility = if (has) View.GONE else View.VISIBLE

        if (has) ticketsAdapter.submit(list)
    }

    /**
     * Profil ezabatzeko baieztapena eta ekintza.
     *
     * Portaera originala:
     * - Confirm -> ProfileTcpService().deleteProfile(userId)
     * - Success -> session.clear() + LoginActivity (CLEAR_TOP|NEW_TASK) + finish()
     */
    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Kontuz!")
            .setMessage(
                "Ziur zaude zure profila ezabatu nahi duzula?\n" +
                        "Sarrerak mantenduko dira, baina kontua itxiko da."
            )
            .setNegativeButton("Ezeztatu", null)
            .setPositiveButton("Bai, ezabatu") { _, _ ->
                val userId = session.getUserId()
                Log.d(TAG, "Delete confirmed: userId=$userId")

                if (userId <= 0) {
                    Toast.makeText(this, "Ez zaude logueatuta", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                lifecycleScope.launch {
                    val res = ProfileTcpService().deleteProfile(userId)

                    res.fold(
                        onSuccess = {
                            Log.i(TAG, "deleteProfile OK -> session.clear() eta Login-era")
                            session.clear()

                            startActivity(
                                Intent(this@NireProfilaActivity, LoginActivity::class.java)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()
                        },
                        onFailure = { e ->
                            Log.e(TAG, "deleteProfile FAIL: ${e.message}", e)
                            Toast.makeText(
                                this@NireProfilaActivity,
                                "Ezin izan da ezabatu: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
            .show()
    }

    // ---- ProfileController.View ----

    override fun setReadOnlyFields(name: String, email: String) {
        Log.d(TAG, "setReadOnlyFields(): nameLen=${name.length} emailLen=${email.length}")
        tvName.text = name
        tvEmail.text = email
    }

    override fun setEditFields(name: String, email: String) {
        Log.d(TAG, "setEditFields(): nameLen=${name.length} emailLen=${email.length}")
        etName.setText(name)
        etEmail.setText(email)

        // Portaera originala: pasahitzak hutsik sartzean
        etPass.setText("")
        etPass2.setText("")
    }

    override fun getEditedName(): String = etName.text?.toString().orEmpty()

    override fun getEditedEmail(): String = etEmail.text?.toString().orEmpty()

    override fun getEditedPass1(): String = etPass.text?.toString().orEmpty()

    override fun getEditedPass2(): String = etPass2.text?.toString().orEmpty()

    override fun setEditMode(editing: Boolean) {
        Log.d(TAG, "setEditMode(): $editing")

        tvName.visibility = if (editing) View.GONE else View.VISIBLE
        tvEmail.visibility = if (editing) View.GONE else View.VISIBLE

        tilName.visibility = if (editing) View.VISIBLE else View.GONE
        tilEmail.visibility = if (editing) View.VISIBLE else View.GONE

        tvPassLabel.visibility = if (editing) View.VISIBLE else View.GONE
        tilPass.visibility = if (editing) View.VISIBLE else View.GONE
        tilPass2.visibility = if (editing) View.VISIBLE else View.GONE
    }

    override fun isEditMode(): Boolean = tilName.visibility == View.VISIBLE

    override fun setEditButtonText(text: String) {
        Log.d(TAG, "setEditButtonText(): $text")
        btnEdit.text = text
    }

    override fun focusName() {
        Log.d(TAG, "focusName()")
        etName.requestFocus()
    }

    override fun setLoading(loading: Boolean) {
        Log.d(TAG, "setLoading(): $loading")

        btnEdit.isEnabled = !loading

        btnEdit.text = if (loading) {
            "..."
        } else {
            if (isEditMode()) "Gorde" else "Editatu"
        }
    }

    override fun showMessage(msg: String) {
        Log.d(TAG, "showMessage(): $msg")
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun setEditEnabled(enabled: Boolean) {
        Log.d(TAG, "setEditEnabled(): $enabled")
        btnEdit.isEnabled = enabled
    }
}