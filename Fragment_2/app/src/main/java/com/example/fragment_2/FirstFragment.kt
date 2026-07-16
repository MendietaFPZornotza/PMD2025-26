package com.example.fragment_2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class FirstFragment : Fragment() {
    lateinit var Bidali: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        Bidali = view.findViewById<Button>(R.id.btnAldatu)
        Bidali.setOnClickListener {
            Log.d("FragmentTest", "Botoia sakatu da!")
            // Bigarren fragment-era pasatu
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SecondFragment())
                .addToBackStack(null) //HEMEN gakoa! Atzera botoiak aurreko fragment-era eramango du
                .commit()
        }

        return view
    }
}