package com.example.taskmanager.model

// Zereginaren datuak gordetzeko data class
data class Task(
    val title: String,
    val type: String,
    val priority: String,
    val extras: List<String>
)

class TaskModel {

    // Hautatutako zereginak gordetzeko lista
    private val taskList = mutableListOf<Task>()

    // Zeregin bat gehitu
    fun addTask(title: String, type: String, priority: String, extras: List<String>) {
        taskList.add(Task(title, type, priority, extras))
    }

    // Hautatutako zeregin guztiak lortu
    fun getTasks(): List<Task> {
        return taskList
    }
}