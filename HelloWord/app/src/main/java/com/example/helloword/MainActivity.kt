package com.example.helloword

import Persona
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helloword.ui.theme.HelloWordTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val hellotext=findViewById<TextView>(R.id.txIzenburua);
        hellotext.text="Hello world";
        /*val p1 = Persona("Ane", 22)
        p1.agurtu(this)*/
                /*setContent {
            HelloWordTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }*/

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Surface(color= Color.Blue) {
        Text(
            text = "Hello, my name is $name!",
            modifier = modifier.padding(30.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HelloWordTheme {
        Greeting("Lorea")
    }
}