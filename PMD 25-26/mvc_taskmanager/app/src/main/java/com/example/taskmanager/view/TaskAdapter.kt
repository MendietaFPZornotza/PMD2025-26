package com.example.taskmanager.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.model.Task

// Adapter-a RecyclerView-ari esaten dion "nola erakutsi".
// View-ren zatia da MVCn: UI elementuak sortu eta datuekin bete egiten ditu.
class TaskAdapter : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // Adapter-en barruan gordetzen den zerrenda mutable bat:
    // Honek ditu RecyclerView-an erakutsi behar dituen Task objektuak.
    private val tasks = mutableListOf<Task>()

    // VIEWHOLDER: elementu bakoitzarentzako "ontzi" bat da.
    // ViewHolder batek elementu grafikoaren erreferentziak gordetzen ditu
    // (adibidez TextView-ak), berrerabilpena errazteko eta findViewById gutxiago egiteko.
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // item_task.xml-eko TextView baten erreferentzia gordetzen dugu hemen.
        val textInfo: TextView = itemView.findViewById(R.id.textTaskInfo)
    }

    // onCreateViewHolder: ViewHolder bat sortzen den tokia.
    // Hemen "inflate" egiten dugu item layout-a (item_task.xml) eta
    // ViewHolder berri bat itzultzen dugu.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // LayoutInflater-ek XML bat "View" objektu bihurtzen du.
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        // Sortutako view-a hartuta, ViewHolder bat itzuli.
        return TaskViewHolder(view)
    }

    // onBindViewHolder: hemen lotzen ditugu datuak eta ikuspegia.
    // Position-ean dagoen Task objektua hartzen dugu eta ViewHolder-eko
    // TextView-en testua ezartzen dugu.
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        // Hemen eraikitako stringa TextView-ean jartzen dugu.
        holder.textInfo.text = buildString {
            append("Zeregina: ${task.title}\n")
            append("Mota: ${task.type}\n")
            append("Lehentasuna: ${task.priority}\n")
            if (task.extras.isNotEmpty()) append("Osagai gehigarriak: ${task.extras.joinToString(", ")}")
        }
    }

    // getItemCount: RecyclerView-k zenbat elementu erakutsi behar dituen jakiteko erabiltzen du.
    override fun getItemCount(): Int = tasks.size

    // submitList: metodo erabilgarria datu berriak pasatzeko.
    // Hemen tasks zerrenda eguneratzen dugu eta notifyDataSetChanged() deitzen dugu
    // RecyclerView guztia berriro marraztu dezan.
    // (Oharrak behean: DiffUtil erabilita optimizatu daiteke.)
    fun submitList(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
}