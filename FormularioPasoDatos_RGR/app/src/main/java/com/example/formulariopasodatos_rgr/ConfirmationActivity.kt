package com.example.formulariopasodatos_rgr

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.formulariopasodatos_rgr.databinding.ConfirmationActivityBinding

class ConfirmationActivity : AppCompatActivity() {

    private lateinit var binding: ConfirmationActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ConfirmationActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}











