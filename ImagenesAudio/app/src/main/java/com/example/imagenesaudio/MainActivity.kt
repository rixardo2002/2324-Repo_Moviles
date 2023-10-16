package com.example.imagenesaudio

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.imagenesaudio.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var mediaplayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaplayer = MediaPlayer.create(this,R.raw.himno)
        binding.imageButton.setOnClickListener(){

            //binding.balon.setImageResource(R.drawable.ic_sport1)

            mediaplayer.start()
        }
        binding.imageButton2.setOnClickListener(){
            //binding.balon.setImageResource(R.drawable.ic_sport2)
            mediaplayer.stop()
            mediaplayer = MediaPlayer.create(this,R.raw.himno)
        }


    }
}