package com.example.simondice

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import com.example.simondice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButtonR.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.imageButtonR.setBackgroundColor(R.color.rojoBrillante)
                }
                MotionEvent.ACTION_UP -> {
                    // Aquí puedes restaurar la imagen a la original
                    binding.imageButtonR.setBackgroundColor(R.color.rojo)                }
            }
            false // Devuelve false para que el evento se propague
        }

    }



}