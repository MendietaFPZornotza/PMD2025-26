package com.example.mvc_counter.controller

import com.example.mvc_counter.model.CounterModel

class CounterController {

    interface CounterView {
        fun showCount(count: Int)

          }

    private val model = CounterModel() // Kontroladoreak sortzen du modeloa
    private var view: CounterView? = null

    init {
        model.addListener(object : CounterModel.Listener {
            override fun onCountChanged(newCount: Int) {
                view?.showCount(newCount)
            }
        })
    }

    fun attachView(v: CounterView) {
        view = v
        view?.showCount(model.getCount()) // hasierako balioa erakutsi
    }

    fun detachView() {
        view = null
    }

    fun onIncrementClicked() = model.increment()
    fun onDecrementClicked() = model.decrement()
    fun onResetClicked() = model.reset()
}