package com.example.mvc_passwordapp.controller

import com.example.mvc_passwordapp.model.PasswordModel

// CONTROLLER: View eta Model artean bitartekari garbia.
// Oso garrantzitsua: Controller-ak bere barnean instantziatzen du Model-a,
// beraz View-k ez du Model-era jotzen edo instantziatzen.
class PasswordController(private val view: PasswordView) {

    // Model hemen sortzen da; View-k ez du sarbiderik Model-era.
    private val model = PasswordModel()

    // View-k inplementatu behar duen interfaze sinplea
    interface PasswordView {
        fun showSuccess()   // Pasahitza zuzena denean deituko da (View-k berrikusi eta abiatuko du WelcomeActivity)
        fun showError()     // Pasahitza okerra denean deituko da (View-k Toast erakutsiko du)
    }

    // Pasahitza egiaztatzeko metodo publiko bat (View-etik deitua)
    fun validatePassword(input: String) {
        if (model.checkPassword(input)) {
            // Model-ekin komunikatu ondoren, View informatu arrakastaz
            view.showSuccess()
        } else {
            // Pasahitz okerra bada, View informatu erroreaz
            view.showError()
        }
    }
}