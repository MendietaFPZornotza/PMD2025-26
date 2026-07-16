package com.example.taskmanager.controller

import android.content.Context
import android.widget.Toast
import com.example.taskmanager.model.Task
import com.example.taskmanager.model.TaskModel
import com.example.taskmanager.view.MainActivity

// Controller: Model eta View arteko komunikazioa kudeatzen du
class TaskController(private val view: TaskView, private val context: Context) {

    private val model = TaskModel()

    // View interfazeak inplementatu beharreko metodoak
    interface TaskView {
        fun updateRecyclerView(tasks: List<Task>)
    }

    // Zeregin bat gehitu eta View eguneratu
    fun addTask(title: String, type: String, priority: String, extras: List<String>) {
        if (title.isEmpty()) {
            Toast.makeText(context, "Mesedez, sartu zereginaren izena", Toast.LENGTH_SHORT).show()
            return
        }

        model.addTask(title, type, priority, extras)
        updateView()

        // Aukeraketa baieztatzeko Toast
        Toast.makeText(context, "Zeregina gehitu da!", Toast.LENGTH_SHORT).show()
    }

    // View eguneratu RecyclerView-rekin
    private fun updateView() {
        val tasks = model.getTasks()
        view.updateRecyclerView(tasks)
    }
}