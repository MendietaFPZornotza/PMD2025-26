package com.example.mvc_counter.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.mvc_counter.R
import com.example.mvc_counter.controller.CounterController
import com.example.mvc_counter.databinding.ActivityMainBinding
import com.example.mvc_counter.model.CounterModel

//class MainActivity : AppCompatActivity(), CounterController.CounterView {

//    private lateinit var tvCount: TextView
//    private lateinit var btnInc: Button
//    private lateinit var btnDec: Button
//    private lateinit var btnReset: Button
//
//    private lateinit var controller: CounterController
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        tvCount = findViewById(R.id.tvCount)
//        btnInc = findViewById(R.id.btnInc)
//        btnDec = findViewById(R.id.btnDec)
//        btnReset = findViewById(R.id.btnReset)
//
//        controller = CounterController() // Controller sortzen du
//        //Hasierako balioa ikusteko
//        controller.attachView(this)
//
//        btnInc.setOnClickListener { controller.onIncrementClicked() }
//        btnDec.setOnClickListener { controller.onDecrementClicked() }
//        btnReset.setOnClickListener { controller.onResetClicked() }
//    }
//
//    override fun showCount(count: Int) {
//        tvCount.text = count.toString()
//    }
//
//      override fun onDestroy() {
//        super.onDestroy()
//        controller.detachView()
//    }
//}

class MainActivity : AppCompatActivity(), CounterController.CounterView {

    private lateinit var binding: ActivityMainBinding
    private lateinit var controller: CounterController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View Binding erabiliz layout-a kargatu
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Controller sortu eta view-a konektatu
        controller = CounterController()
        controller.attachView(this)

        // Botoien ekintzak binding bidez
        binding.btnInc.setOnClickListener { controller.onIncrementClicked() }
        binding.btnDec.setOnClickListener { controller.onDecrementClicked() }
        binding.btnReset.setOnClickListener { controller.onResetClicked() }


    }

    override fun showCount(count: Int) {
    // TextView eguneratu binding bidez
        binding.tvCount.text = count.toString()
    }



    override fun onDestroy() {
        super.onDestroy()
        controller.detachView()
    }
}
