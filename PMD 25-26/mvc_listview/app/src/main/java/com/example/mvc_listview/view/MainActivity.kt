package com.example.mvc_listview.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
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
import com.example.mvc_listview.controller.ListController
import com.example.mvc_listview.ui.theme.Mvc_listviewTheme
import com.example.mvc_listview.R

class MainActivity : ComponentActivity(), ListController.ListView {

    private lateinit var controller: ListController
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var listView: ListView
    private lateinit var editText: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Layout-eko elementuak hartu
        editText = findViewById(R.id.editTextItem)
        addButton = findViewById(R.id.buttonAdd)
        listView = findViewById(R.id.listViewItems)

        // Controller sortu (modela barnean dauka)
        controller = ListController(this)

        // Adapter-a prestatu ListView-rako
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        listView.adapter = adapter

        // "Gehitu" botoia: testua Controller-era bidali
        addButton.setOnClickListener {
            val text = editText.text.toString()
            controller.addItem(text)
        }

        // ListView-ko elementu bati klik egitean: kendu
        listView.setOnItemClickListener { _, _, position, _ ->
            controller.removeItem(position)
        }

    }

    // Zerrenda eguneratzeko metodoa
    override fun updateList(items: List<String>) {
        adapter.clear()
        adapter.addAll(items)
        adapter.notifyDataSetChanged()
    }

    // Testu-eremua garbitzeko metodoa
    override fun clearInput() {
        editText.text.clear()
    }
}