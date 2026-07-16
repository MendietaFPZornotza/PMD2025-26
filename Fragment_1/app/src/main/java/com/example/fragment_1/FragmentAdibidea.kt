package com.example.fragment_1

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class FragmentAdibidea : Fragment() {
    lateinit var Bidali: Button
    // Aldagai pribatua deklaratu
    private var listener: OnFragmentInteractionListener? = null

    // Interfazea: Activity-ak inplementatu beharko du
    interface OnFragmentInteractionListener {
        fun onMezuaBidali(mezua: String)
    }

    // Fragmenta Activity-ra konektatzen denean listener esleitu
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnFragmentInteractionListener
    }

    // Fragmentaren diseinua sortu eta botoia konfiguratu
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_adibidea, container, false)

        Bidali = view.findViewById<Button>(R.id.btnBidali)
        Bidali.setOnClickListener {
            listener?.onMezuaBidali("Fragmentetik mezua bidali dut!")
        }
        return view
    }

    // Fragmenta deslotzean listener garbitu (praktika ona)
    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
