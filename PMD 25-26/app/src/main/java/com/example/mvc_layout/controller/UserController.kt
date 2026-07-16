package com.example.mvc_layout.controller

import com.example.mvc_layout.model.UserModel


class UserController {

    private val userList = mutableListOf<UserModel>()

    fun gehituErabiltzailea(izena: String, abizena: String, adina: Int) {
        val user = UserModel(izena, abizena, adina)
        userList.add(user)
    }

    fun lortuErabiltzaileak(): List<UserModel> {
        return userList
    }
}