package com.example.mvc_vistacontrollerbatera.model

class CounterModel {
    private var count: Int = 0

    interface Listener {
        fun onCountChanged(newCount: Int)
    }

    private val listeners = mutableListOf<Listener>()

    fun addListener(l: Listener) {
        if (!listeners.contains(l)) listeners.add(l)
    }

    fun removeListener(l: Listener) {
        listeners.remove(l)
    }

    fun getCount(): Int = count

    fun increment() {
        count++
        notifyListeners()
    }

    fun decrement() {
        count--
        notifyListeners()
    }

    fun reset() {
        count = 0
        notifyListeners()
    }

    private fun notifyListeners() {
        for (l in listeners) l.onCountChanged(count)
    }
}