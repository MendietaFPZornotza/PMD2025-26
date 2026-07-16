package com.example.examenandroid.controller

import android.content.Context
import com.example.examenandroid.model.Evento
import java.io.File

class EventoController(private val context: Context) {

    private val fileName = "eventos.txt"
    private val file = File(context.filesDir, fileName)
    private var eventos: MutableList<Evento> = mutableListOf()

    init {
        if (file.exists()) {
            val lines = file.readLines()
            eventos = lines.mapNotNull { Evento.fromLine(it) }.toMutableList()
        } else {
            file.createNewFile()
        }

        // Fitxategia hutsa bada, hiru evento adibide sortu
        if (eventos.isEmpty()) {
            eventos.addAll(
                listOf(
                    Evento(1, "Kontzertu", "Rock kontzertu", "2026-01-30", "Areto A", "Musika"),
                    Evento(2, "Tailer", "Margo tailerra", "2026-02-05", "Kultur Etxea", "Arte"),
                    Evento(3, "Bilera", "Talde bilera", "2026-02-10", "Bulegoa", "Lan")
                )
            )
            saveToFile()
        }
    }

    private fun saveToFile() {
        file.writeText(eventos.joinToString("\n") { it.toLine() })
    }

    // Eventoak lortu
    fun obtenerEventos(): List<Evento> = eventos

    // MVC puro: Vista soilik pasatzen ditu string-ak
    fun agregarEvento(nombre: String, descripcion: String, fecha: String, lugar: String, tipo: String) {
        val evento = Evento(generarId(), nombre, descripcion, fecha, lugar, tipo)
        eventos.add(evento)
        saveToFile()
    }
    // Eventoak eguneratu

    fun actualizarEvento(id: Int, nombre: String, descripcion: String, fecha: String, lugar: String, tipo: String) {
        val index = eventos.indexOfFirst { it.id == id }
        if (index != -1) {
            eventos[index] = Evento(id, nombre, descripcion, fecha, lugar, tipo)
            saveToFile()
        }
    }
    // Eventoak Ezabatu
    fun eliminarEventoPorId(id: Int) {
        eventos.removeAll { it.id == id }
        saveToFile()
    }

    fun generarId(): Int = if (eventos.isEmpty()) 1 else eventos.maxOf { it.id } + 1
}