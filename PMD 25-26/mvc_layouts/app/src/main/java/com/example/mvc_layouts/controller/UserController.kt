package com.example.mvc_layouts.controller

import com.example.mvc_layouts.model.UserModel

/**
 * View eta Model arteko bitartekaria
 */
class UserController {

    private val model = UserModel()

    /**
     * Model-era erabiltzailea gehitu
     */
    fun gehituErabiltzailea(izena: String, abizena: String, adina: Int) {
        model.gehituErabiltzailea(izena, abizena, adina)
    }

    /**
     * Model-etik erabiltzaile guztiak lortu
     */
    fun lortuErabiltzaileak(): List<UserModel.Erabiltzailea> {
        return model.lortuErabiltzaileak()
    }
}