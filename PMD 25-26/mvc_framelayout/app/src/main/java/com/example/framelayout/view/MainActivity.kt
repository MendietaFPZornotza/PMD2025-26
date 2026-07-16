package com.example.framelayout.view

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.framelayout.controller.ImageController
import com.example.framelayout.ui.theme.FramelayoutTheme
import com.example.framelayout.R
// VIEW: FrameLayout erabiliz irudia erakusten du
class MainActivity : ComponentActivity(), ImageController.ImageViewLayer {

    private lateinit var controller: ImageController
    private lateinit var imageView: ImageView
    private lateinit var buttonToggle: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        buttonToggle = findViewById(R.id.buttonToggle)

        controller = ImageController(this)

        // Hasieran ezkutatu
        imageView.visibility = ImageView.INVISIBLE

        // Botoia sakatzean Controller-era deitu
        buttonToggle.setOnClickListener { controller.toggleImage() }
    }

    override fun showImage() {
        imageView.visibility = ImageView.VISIBLE
        buttonToggle.text = "Ezkutatu irudia"
    }

    override fun hideImage() {
        imageView.visibility = ImageView.INVISIBLE
        buttonToggle.text = "Erakutsi irudia"
    }
}