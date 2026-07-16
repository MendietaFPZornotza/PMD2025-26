package com.example.erronka_proba.controller.profile

import android.util.Log
import com.example.erronka_proba.data.ConnectivityState
import com.example.erronka_proba.data.tcp.ProfileTcpService
import com.example.erronka_proba.data.tcp.SessionManager
import com.example.erronka_proba.model.domain.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Fitxategia: ProfileController.kt
 *
 * Zertarako da?
 * - Profilaren pantailako logika kudeatzeko (ikusi + editatu + gorde).
 *
 * Zer egiten du?
 * - onCreate():
 *   - Session-etik datuak pantailan jarri (read-only + edit fields).
 *   - Online badago: server-etik profila kargatu eta session eguneratu.
 *   - Offline bada: editatzeko aukera desgaitu eta mezua.
 * - onEditButtonClicked():
 *   - Offline -> ezin eguneratu.
 *   - Edit moduan ez badago -> edit modua aktibatu.
 *   - Edit moduan badago -> balidazioak + updateProfile() + UI eguneratu.
 *
 * Nola erabiltzen da?
 * - scope.launch erabiltzen du sareko operazioetarako.
 * - View interfazeak UI-ko elementuak kontrolatzeko funtzioak ematen ditu.
 *
 * Log-ak:
 * - Flow-a: create, offline/online, load, edit mode, update, error...
 */
class ProfileController(
    private val view: View,
    private val scope: CoroutineScope,
    private val profileService: ProfileTcpService,
    private val session: SessionManager
) {

    companion object {
        private const val TAG = "ProfileController"
    }

    interface View {
        fun setLoading(loading: Boolean)
        fun showMessage(msg: String)
        fun setEditEnabled(enabled: Boolean)

        fun setReadOnlyFields(name: String, email: String)
        fun setEditFields(name: String, email: String)

        fun getEditedName(): String
        fun getEditedEmail(): String
        fun getEditedPass1(): String
        fun getEditedPass2(): String

        fun setEditMode(editing: Boolean)
        fun isEditMode(): Boolean
        fun setEditButtonText(text: String)
        fun focusName()
    }

    /**
     * Profile pantaila irekitzean:
     * - Session datuak jarri
     * - Online badago, server-etik profila kargatu
     */
    fun onCreate() {
        Log.i(TAG, "onCreate(): profila hasieratzen")

        val sName = session.getName().ifBlank { "-" }
        val sEmail = session.getEmail().ifBlank { "-" }

        // Lehenengo: UI-ri session-eko datuak erakutsi
        view.setReadOnlyFields(sName, sEmail)
        view.setEditFields(session.getName(), session.getEmail())

        val email = session.getEmail()
        if (email.isBlank()) {
            Log.w(TAG, "onCreate(): session email hutsik -> ez dago zer kargatu")
            return
        }

        // Offline modua: ezin eguneratu
        if (!ConnectivityState.online) {
            Log.w(TAG, "onCreate(): offline modua -> edit desaktibatu")
            view.showMessage("Offline modua: ezin da profila eguneratu")
            view.setEditEnabled(false)
        }

        // Profila server-etik kargatu
        view.setLoading(true)
        Log.d(TAG, "onCreate(): profileService.getProfile(email=$email)")

        scope.launch {
            val res = profileService.getProfile(email)

            view.setLoading(false)
            Log.d(TAG, "getProfile() amaituta")

            res.fold(
                onSuccess = { user ->
                    Log.i(TAG, "getProfile() success: id=${user.id}, name=${user.name}")

                    // Id-a eguneratzeko logika originala mantentzen da
                    val currentId = session.getUserId()
                    val newId = if (user.id > 0) user.id else currentId

                    session.saveUser(newId, user.name, user.email)

                    view.setReadOnlyFields(user.name, user.email)
                    view.setEditFields(user.name, user.email)

                    ConnectivityState.online = true
                },
                onFailure = { e ->
                    Log.e(TAG, "getProfile() failure: ${e.message}", e)
                    view.showMessage("Ezin izan da profila kargatu: ${e.message}")
                    ConnectivityState.online = false
                    view.showMessage("Konexiorik ez: ezin da eguneratu edo erosi")
                }
            )
        }
    }

    /**
     * "Editatu / Gorde" botoiaren logika.
     * - Offline: ez da onartzen.
     * - Edit moduan ez badago: edit aktibatu.
     * - Edit moduan badago: balidazioak + updateProfile.
     */
    fun onEditButtonClicked() {
        Log.d(TAG, "onEditButtonClicked(): editMode=${view.isEditMode()}, online=${ConnectivityState.online}")

        if (!ConnectivityState.online) {
            view.showMessage("Konexiorik ez: ezin da eguneratu")
            Log.w(TAG, "Eguneratzea blokeatuta: offline")
            return
        }

        // Edit modura sartu (Gorde modura pasatu)
        if (!view.isEditMode()) {
            Log.i(TAG, "Edit modua aktibatzen")
            view.setEditFields(session.getName(), session.getEmail())
            view.setEditMode(true)
            view.setEditButtonText("Gorde")
            view.focusName()
            return
        }

        // Hemen: edit moduan gaude eta "Gorde" sakatu da
        val oldEmail = session.getEmail()
        if (oldEmail.isBlank()) {
            Log.w(TAG, "Ez dago logueatuta (oldEmail hutsik)")
            view.showMessage("Ez zaude logueatuta")
            return
        }

        val editedName = view.getEditedName().trim()
        val editedEmail = view.getEditedEmail().trim()

        val p1 = view.getEditedPass1()
        val p2 = view.getEditedPass2()

        Log.d(TAG, "Gorde: editedName='$editedName', editedEmail='$editedEmail', pass1Blank=${p1.isBlank()}, pass2Blank=${p2.isBlank()}")

        // Pasahitza: aukerakoa. Zerbaitek idatzita badago, berdinak izan behar dute.
        val passwordToUpdate: String? = when {
            p1.isBlank() && p2.isBlank() -> null
            p1 != p2 -> {
                Log.w(TAG, "Pasahitzak ez dira berdinak")
                view.showMessage("Pasahitzak ez dira berdinak")
                return
            }
            else -> p1
        }

        val nameToUpdate: String? = if (editedName.isBlank()) null else editedName
        val emailToUpdate: String? = if (editedEmail.isBlank()) null else editedEmail

        view.setLoading(true)
        Log.d(TAG, "updateProfile(): oldEmail=$oldEmail nameToUpdate=$nameToUpdate emailToUpdate=$emailToUpdate passUpdate=${passwordToUpdate != null}")

        scope.launch {
            val res = profileService.updateProfile(
                oldEmail = oldEmail,
                newName = nameToUpdate,
                newEmail = emailToUpdate,
                newPassword = passwordToUpdate
            )

            view.setLoading(false)

            res.fold(
                onSuccess = { user: User ->
                    Log.i(TAG, "updateProfile() success: id=${user.id}, email=${user.email}")

                    session.saveUser(user.id, user.name, user.email)

                    view.setReadOnlyFields(user.name, user.email)
                    view.setEditFields(user.name, user.email)

                    view.setEditMode(false)
                    view.setEditButtonText("Editatu")
                    view.showMessage("Profila eguneratuta")
                },
                onFailure = { e ->
                    Log.e(TAG, "updateProfile() failure: ${e.message}", e)
                    view.showMessage("Ezin izan da gorde: ${e.message}")
                }
            )
        }
    }
}