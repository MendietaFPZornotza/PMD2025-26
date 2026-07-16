package com.example.taskmanager.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.controller.TaskController
import com.example.taskmanager.R
import com.example.taskmanager.model.Task

class MainActivity : ComponentActivity(),  TaskController.TaskView{
    private lateinit var controller: TaskController
    private lateinit var spinnerType: Spinner
    private lateinit var radioLow: RadioButton
    private lateinit var radioMedium: RadioButton
    private lateinit var radioHigh: RadioButton
    private lateinit var checkNote: CheckBox
    private lateinit var checkRepeat: CheckBox
    private lateinit var editTitle: EditText
    private lateinit var buttonAdd: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Elementuak lotu
        spinnerType = findViewById(R.id.spinnerType)
        radioLow = findViewById(R.id.radioLow)
        radioMedium = findViewById(R.id.radioMedium)
        radioHigh = findViewById(R.id.radioHigh)
        checkNote = findViewById(R.id.checkNote)
        checkRepeat = findViewById(R.id.checkRepeat)
        editTitle = findViewById(R.id.editTitle)
        buttonAdd = findViewById(R.id.buttonAdd)
        recyclerView = findViewById(R.id.recyclerViewTasks)

        // Controller sortu
        controller = TaskController(this, this)

        // Spinner-a bete
        val types = listOf("Lan", "Etxeko", "Kirol", "Bestelako")
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = adapterSpinner

        // RecyclerView prestatu
       // Hemen sortzen dugu adapter objektua (RecyclerView-n erabiliko dena).
        //TaskAdapter da zure RecyclerView.Adapter pertsonalizatua, eta honek esango dio nola marraztu elementu bakoitza (item_task.xml).
        adapter = TaskAdapter()
        //erakutsi elementuak zutabe batean (bertikalean).
        recyclerView.layoutManager = LinearLayoutManager(this)
        //RecyclerView-ri esleitzen dio zein adapter erabiliko duen.
        recyclerView.adapter = adapter


        // Botoiaren ekintza
        buttonAdd.setOnClickListener {
            val title = editTitle.text.toString()
            val type = spinnerType.selectedItem.toString()
            val priority = when {
                radioLow.isChecked -> "Baxua"
                radioMedium.isChecked -> "Ertaina"
                radioHigh.isChecked -> "Altua"
                else -> ""
            }

            val extras = mutableListOf<String>()
            if (checkNote.isChecked) extras.add("Oharra")
            if (checkRepeat.isChecked) extras.add("Errepikagarria")

            controller.addTask(title, type, priority, extras)
        }
    }

    // RecyclerView eguneratzeko metodoa
    override fun updateRecyclerView(tasks: List<Task>) {
        adapter.submitList(tasks)
    }
}



