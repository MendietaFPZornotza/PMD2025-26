package com.example.erronka_proba.model

data class Seat(val code: String, var state: State) {
    enum class State { FREE, SELECTED, OCCUPIED }
}