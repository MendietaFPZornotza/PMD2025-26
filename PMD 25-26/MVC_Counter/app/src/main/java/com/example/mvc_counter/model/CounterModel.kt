package com.example.mvc_counter.model

class CounterModel {
    private var count: Int = 0
    // Modeloak zenbaketa gordetzen du (egoera), hasieran 0 balioarekin.

    interface Listener {
        fun onCountChanged(newCount: Int)
    }
    // Modeloko aldaketak entzuten dituzten objektuek interfaz hau inplementatu behar dute.

    private val listeners = mutableListOf<Listener>()
    // Aldaketak jasoko dituzten entzuleen zerrenda.

    fun addListener(l: Listener) {
        if (!listeners.contains(l)) listeners.add(l)
    }
    // Entzule bat gehitzen du, baldin eta oraindik ez badago.

    fun removeListener(l: Listener) {
        listeners.remove(l)
    }
    // Entzule bat kentzen du (adibidez, view-a desaktibatzean).

    fun increment() {
        count++
        notifyListeners()
    }
    // Balioa handitu eta entzuleei jakinarazi.

    fun decrement() {
        count--
        notifyListeners()
    }
    // Balioa txikitu eta entzuleei jakinarazi.

    fun reset() {
        count = 0
        notifyListeners()
    }
    // Balioa berrabiarazi eta entzuleei aldaketa bidali.

    private fun notifyListeners() {
        for (l in listeners) l.onCountChanged(count)
    }
    // Aldaketa gertatzean, entzule guztiei mezu bat bidaltzen zaie.
    // Kontroladoreak bakarrik erabil dezake
    fun getCount(): Int = count

}