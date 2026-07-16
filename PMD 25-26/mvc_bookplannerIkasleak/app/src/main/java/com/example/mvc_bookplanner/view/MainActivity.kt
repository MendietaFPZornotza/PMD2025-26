package com.example.mvc_bookplanner.view

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvc_bookplanner.controller.BookController
import com.example.mvc_bookplanner.ui.theme.Mvc_bookplannerTheme
import com.example.mvc_bookplanner.R
import android.widget.ArrayAdapter
import com.example.mvc_bookplanner.model.Book

class MainActivity : ComponentActivity(), BookController.BookView {

    private lateinit var controller: BookController
    private lateinit var spinnerGenre: Spinner
    private lateinit var radioPaper: RadioButton
    private lateinit var radioDigital: RadioButton
    private lateinit var checkOffer: CheckBox
    private lateinit var checkGuide: CheckBox
    private lateinit var buttonAdd: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Elementuak lotu
        spinnerGenre = findViewById(R.id.spinnerGenre)
        radioPaper = findViewById(R.id.radioPaper)
        radioDigital = findViewById(R.id.radioDigital)
        checkOffer = findViewById(R.id.checkOffer)
        checkGuide = findViewById(R.id.checkGuide)
        buttonAdd = findViewById(R.id.buttonAdd)
        recyclerView = findViewById(R.id.recyclerViewBooks)

        // Controller sortu
        controller = BookController(this, this)

        // Spinner-a bete
        val genres = listOf("Nobela", "Zientzia-Fikzioa", "Historia", "Poesia")
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, genres)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenre.adapter = adapterSpinner

        // RecyclerView prestatu
        adapter = BookAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Botoiaren ekintza
        buttonAdd.setOnClickListener {
            val genre = spinnerGenre.selectedItem.toString()
            val format = when {
                radioPaper.isChecked -> "Paperezkoa"
                radioDigital.isChecked -> "Digitala"
                else -> ""
            }

            val extras = mutableListOf<String>()
            if (checkOffer.isChecked) extras.add("Eskaintza berezia")
            if (checkGuide.isChecked) extras.add("Liburu-gida")

            controller.addBook(genre, format, extras)

            // Inputak berriro hasieratu
            spinnerGenre.setSelection(0)         // Spinner lehenengo elementura itzul
            radioPaper.isChecked = false          // RadioButton guztiak desmarkatu
            radioDigital.isChecked = false          // RadioButton guztiak desmarkatu
            checkOffer.isChecked = false          // CheckBox guztiak desmarkatu
            checkGuide.isChecked = false          // CheckBox guztiak desmarkatu

        }
    }

    // RecyclerView eguneratzeko metodoa
    /*books: List<Book> → datuak Controller edo Model-etik jasoak dira.
    adapter.submitList(books) → View-ra doa, RecyclerView eguneratzeko.
    Gakoa: View ez da Model-era jotzen; Controller-ek pasatzen dizkio datuak.*/
    override fun updateRecyclerView(books: List<Book>) {
        adapter.submitList(books)
    }
}

