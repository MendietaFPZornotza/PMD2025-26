package com.example.mvc_checkbox3.model

class FoodModel {

    data class Food(val izena: String, val prezioa: Double)

    private val hautatutakoJakiak = mutableListOf<Food>()

    fun setSelections(list: List<Food>) {
        hautatutakoJakiak.clear()
        hautatutakoJakiak.addAll(list)
    }

    fun getSelections(): List<Food> = hautatutakoJakiak

    fun kalkulatuGuztira(): Double {
        return hautatutakoJakiak.sumOf { it.prezioa }
    }
}