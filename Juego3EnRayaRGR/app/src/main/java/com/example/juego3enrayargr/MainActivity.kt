package com.example.juego3enrayargr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.juego3enrayargr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var contador=1
    var ganadas1=0
    var ganadas2=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pulsarBotonesJug()

    }

    private fun pulsarBotonesJug(){

        var juegoterminado=false;
        val imagenes = arrayOf(
            binding.imageButton00,binding.imageButton01,binding.imageButton02,
            binding.imageButton10,binding.imageButton11,binding.imageButton12,
            binding.imageButton20,binding.imageButton21,binding.imageButton22
        )

        for (imagen in imagenes) {

                imagen.setOnClickListener() {
                    if (imagen.drawable.constantState == ContextCompat.getDrawable(this,
                            R.drawable.inicio)?.constantState
                        && !juegoterminado) {
                        if (contador % 2 != 0) {

                            imagen.setImageResource(R.drawable.naruto)

                        } else {
                            imagen.setImageResource(R.drawable.sasuke)

                        }
                        contador++
                    }

                    if (jugadasGanadorasJugador1() && !juegoterminado) {

                        Toast.makeText(this, "El ganador es JUGADOR 1 !!!!", Toast.LENGTH_SHORT).show()

                        juegoterminado=true
                        ganadas1++
                        binding.cont1.setText(ganadas1.toString())

                    } else if (jugadasGanadorasJugador2() && !juegoterminado) {
                        Toast.makeText(this, "El ganador es JUGADOR 2 !!!!", Toast.LENGTH_SHORT).show()
                        juegoterminado=true
                        ganadas2++
                        binding.cont2.setText(ganadas2.toString())

                    } else if (contador > 9) {
                        Toast.makeText(this, "Es un EMPATE", Toast.LENGTH_SHORT).show()

                        juegoterminado=true
                    }
                    if (juegoterminado){
                        binding.button.setOnClickListener(){
                            ponerPordefecto()
                            contador=1
                            juegoterminado=false
                        }
                    }
                }
        }

    }

    private fun jugadasGanadorasJugador1(): Boolean{
        val jugada00 = binding.imageButton00.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.naruto)?.constantState
        val jugada01 = binding.imageButton01.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.naruto)?.constantState
        val jugada02 = binding.imageButton02.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.naruto)?.constantState
        val jugada10 = binding.imageButton10.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.naruto)?.constantState
        val jugada11 = binding.imageButton11.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.naruto)?.constantState
        val jugada12 = binding.imageButton12.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.naruto)?.constantState
        val jugada20 = binding.imageButton20.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.naruto)?.constantState
        val jugada21 = binding.imageButton21.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.naruto)?.constantState
        val jugada22 = binding.imageButton22.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.naruto)?.constantState

        if ((jugada00 && jugada01 && jugada02)|| (jugada00 && jugada10 && jugada20)||(jugada00 && jugada11 && jugada22)||
            (jugada01 && jugada11 && jugada21)||(jugada02 && jugada12 && jugada22)||(jugada20 && jugada21 && jugada22)||
                (jugada20 && jugada11 && jugada02) ||(jugada10 && jugada11 && jugada12) ){
            return true;
        }
        return false;
    }
    private fun jugadasGanadorasJugador2(): Boolean{
        val jugada00 = binding.imageButton00.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.sasuke)?.constantState
        val jugada01 = binding.imageButton01.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.sasuke)?.constantState
        val jugada02 = binding.imageButton02.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.sasuke)?.constantState
        val jugada10 = binding.imageButton10.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.sasuke)?.constantState
        val jugada11 = binding.imageButton11.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.sasuke)?.constantState
        val jugada12 = binding.imageButton12.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.sasuke)?.constantState
        val jugada20 = binding.imageButton20.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.sasuke)?.constantState
        val jugada21 = binding.imageButton21.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.sasuke)?.constantState
        val jugada22 = binding.imageButton22.drawable?.constantState == ContextCompat.getDrawable(this, R.drawable.sasuke)?.constantState

        if ((jugada00 && jugada01 && jugada02)|| (jugada00 && jugada10 && jugada20)||(jugada00 && jugada11 && jugada22)||
            (jugada01 && jugada11 && jugada21)||(jugada02 && jugada12 && jugada22)||(jugada20 && jugada21 && jugada22)||
            (jugada20 && jugada11 && jugada02)||(jugada10 && jugada11 && jugada12)){
            return true;
        }
        return false;
    }
    private fun ponerPordefecto(){
        val imagenes = arrayOf(
            binding.imageButton00,binding.imageButton01,binding.imageButton02,
            binding.imageButton10,binding.imageButton11,binding.imageButton12,
            binding.imageButton20,binding.imageButton21,binding.imageButton22
        )
        for (imagen in imagenes){
            imagen.setImageResource(R.drawable.inicio)
            
        }
    }
}