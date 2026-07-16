package com.example.praktika10menu

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
    //una funcion que sobreescriba el menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true//super.onCreateOptionsMenu(menu)

    }
    //una funcion que sobreescriba la funcionpara q actuen nuestros botones
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            R.id.gorde-> {
                Toast.makeText(this,"Gorde botoia sakatu da",Toast.LENGTH_LONG )
                true
            }
            R.id.ireki-> {
                Toast.makeText(this,"Ireki botoia sakatu da",Toast.LENGTH_LONG )
                true
            }
            R.id.irten-> {
                finish()
                true
            }
            else-> return super.onOptionsItemSelected(item)
        }

    }
}