package com.example.dig4634.gpsexample

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DigitalWorldsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_digital_worlds)
        val extras = intent.extras
        if (extras != null) {
            val frame = findViewById<ImageView>(R.id.photo_frame)
            frame.setImageResource(extras.getInt("pictureID"))
            val text = findViewById<TextView>(R.id.caption)
            text.text = extras.getString("caption")
        }
    }
}