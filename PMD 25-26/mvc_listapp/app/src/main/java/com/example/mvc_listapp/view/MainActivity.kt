package com.example.mvc_listapp.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
import com.example.mvc_listapp.controller.ListController
import com.example.mvc_listapp.model.ItemModel
import com.example.mvc_listapp.ui.theme.Mvc_listappTheme
import com.example.mvc_listapp.R

class MainActivity : ComponentActivity(), ListController.ListView {

    private lateinit var txtInput: EditText
    private lateinit var tvList: TextView
    private lateinit var btnAdd: Button
    private lateinit var btnClear: Button

    private lateinit var model: ItemModel
    private lateinit var controller: ListController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // VIEW elementuak hartu
        txtInput = findViewById(R.id.txtInput)
        tvList = findViewById(R.id.tvList)
        btnAdd = findViewById(R.id.btnAdd)
        btnClear = findViewById(R.id.btnClear)

        // MODEL eta CONTROLLER sortu
        model = ItemModel()
        controller = ListController(model, this)

        // Botoiak lotu kontrolatzailearekin
        btnAdd.setOnClickListener {
            val text = txtInput.text.toString()
            controller.onAddClicked(text)
            txtInput.text.clear()
        }

        btnClear.setOnClickListener {
            controller.onClearClicked()
        }
    }

    // Controller - View komunikazioa
    override fun showList(items: List<String>) {
        if (items.isEmpty()) {
            tvList.text = "(Ez dago elementurik)"
        } else {
            tvList.text = items.joinToString("\n• ")
        }
    }

    override fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.onDestroy()
    }
}