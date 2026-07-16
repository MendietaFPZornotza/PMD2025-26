package com.example.praktika11frameestatikoa.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.praktika11frameestatikoa.R
import com.example.praktika11frameestatikoa.databinding.ActivityMainBinding
import com.example.praktika11frameestatikoa.fragments.HomeFragment
import com.example.praktika11frameestatikoa.fragments.ProfileFragment
import com.example.praktika11frameestatikoa.fragments.SupportFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //Vincularnos con la vista
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Una vez viculada la vista podemos usar su contenido
        //vamos a inflar la vista con el fragments que no interesa
        //commit se utiliza para confirmar solicitud
        supportFragmentManager.beginTransaction().add(R.id.frameContainer, HomeFragment()).commit()

        binding.bottomNavMenu.setOnItemSelectedListener {
            when(it.itemId){
                R.id.mHome -> supportFragmentManager.beginTransaction().replace(R.id.frameContainer, HomeFragment()).commit()
                R.id.mProfile -> supportFragmentManager.beginTransaction().replace(R.id.frameContainer, ProfileFragment()).commit()
                R.id.mSupport -> supportFragmentManager.beginTransaction().replace(R.id.frameContainer, SupportFragment()).commit()
                R.id.mExit -> finish()
            }
            true
        }
    }
}