package com.example.mvc_checkbox3.controller

import com.example.mvc_checkbox3.model.FoodModel

class FoodController(private val view: FoodView) {
    private val model = FoodModel()   // el modelo se crea aquí

    interface FoodView {
        fun getSelectedNames(): List<String>
        fun showResult(text: String)
    }

    private val foodPrices = mapOf(
        "Pizza" to 10.0,
        "Sushi" to 12.0,
        "Pasta" to 8.0,
        "Ensalada" to 6.0
    )

    fun onCalculateClicked() {

        val selectedNames = view.getSelectedNames()

        val listaObjetos = mutableListOf<FoodModel.Food>()

        for (name in selectedNames) {
            val precio = foodPrices[name]
            if (precio != null) {
                listaObjetos.add(FoodModel.Food(name, precio))
            }
        }

        model.setSelections(listaObjetos)

        if (listaObjetos.isEmpty()) {
            view.showResult("Ez da ezer hautatu.")
            return
        }

        val total = model.kalkulatuGuztira()
        val names = listaObjetos.joinToString("\n- ") { it.izena }

        val text = "Hautatutako jakiak:\n- $names\n\nGuztira: ${"%.2f".format(total)} €"
        view.showResult(text)
    }
}