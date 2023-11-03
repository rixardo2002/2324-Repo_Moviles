package com.example.menusrgr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast

import com.example.menusrgr.databinding.ActivityVentanaOpcion2Binding
import java.util.Random

class VentanaOpcion2 : AppCompatActivity() {
    lateinit var binding: ActivityVentanaOpcion2Binding
    private val simonButtons = ArrayList<ImageButton>()
    private val simonPattern = ArrayList<ImageButton>()
    private var playerPatternIndex = 0
    private var isSimonPlaying = false
    private var handler = android.os.Handler()
    private var nivel = 1 // Inicializamos el nivel en 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_ventana_opcion1)
        binding = ActivityVentanaOpcion2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar2.title = "    Mi aplicación DAM"
        binding.toolbar2.subtitle = "     Opcion1"
        binding.toolbar2.setLogo(R.drawable.ic_logo)

        //aquí simplemente inflo la toolBaar, pero aún no hay opciones ni botón home.
        setSupportActionBar(binding.toolbar2)

        //en las siguientes líneas hago que aaprezca el botón de atrás (home) y genero su evento
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar2.setNavigationOnClickListener {
            Toast.makeText(this,"Pulsado el retroceso", Toast.LENGTH_SHORT).show()
            finish()
        }
        // Recupera el nombre enviado desde MainActivity
        val nombre = intent.getStringExtra("nombre")

        if (nombre != null) {
            // Hacer algo con el nombre, como mostrarlo en un TextView
            Toast.makeText(this,"Bienvenido "+ nombre, Toast.LENGTH_SHORT).show()

        }

        // Inicializamos ImageButtons y establecemos los botones
        val buttonRojo = binding.imageButtonR
        val buttonVerde = binding.imageButtonV
        val buttonAzul = binding.imageButtonA
        val buttonAmarillo = binding.imageButtonN

        val botonIniciar = binding.button5
        botonIniciar.setOnClickListener {
            if (!isSimonPlaying) {
                iniciarJuegoSimonDice()
            }
        }
        val botonReiniciar = binding.button6
        botonReiniciar.setOnClickListener {
            reiniciarJuegoSimonDice()
        }
        simonButtons.add(buttonRojo)
        simonButtons.add(buttonVerde)
        simonButtons.add(buttonAzul)
        simonButtons.add(buttonAmarillo)

        for (button in simonButtons) {
            button.setOnClickListener {
                if (isSimonPlaying) {
                    manejarEntradaJugador(button)
                }
            }
        }
    }
    private fun iniciarJuegoSimonDice() {
        isSimonPlaying = true
        binding.button5.visibility = View.GONE
        generarPatronSimon()
        mostrarPatronSimon()
        binding.nivel.text = "Nivel $nivel"
    }

    private fun reiniciarJuegoSimonDice() {
        isSimonPlaying = false
        binding.button5.visibility = View.VISIBLE
        simonPattern.clear()
        playerPatternIndex = 0
        nivel = 1 // Reiniciamos el nivel
    }

    private fun generarPatronSimon() {
        val random = Random()
        val indiceButton = random.nextInt(simonButtons.size)
        val simonButton = simonButtons[indiceButton]
        simonPattern.add(simonButton)
    }

    private fun mostrarPatronSimon() {
        val delayBetweenButtons = 1000L // Ajusta el retraso entre cada botón

        for (i in simonPattern.indices) {
            val button = simonPattern[i]
            val delayMillis = (i + 1) * 1000L

            handler.postDelayed({
                // Activar el estado "pressed" para aplicar el selector
                button.isPressed = true

                // Retrasar la desactivación del estado "pressed" para volver al estado normal
                handler.postDelayed({
                    button.isPressed = false
                }, 500)
            }, delayMillis)
        }
    }


    private fun manejarEntradaJugador(button: ImageButton) {
        val buttonEsperado = simonPattern[playerPatternIndex]

        if (button == buttonEsperado) {
            playerPatternIndex++

            if (playerPatternIndex == simonPattern.size) {
                playerPatternIndex = 0
                Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show()

                generarPatronSimon()
                nivel++ // Incrementamos el nivel
                binding.nivel.text = "Nivel $nivel" // Actualizamos el nivel
                if (nivel == 10) {
                    // Has ganado el juego
                    isSimonPlaying = false
                    Toast.makeText(this, "¡HAS GANADOO!", Toast.LENGTH_SHORT).show()

                } else {
                    // Mostrar la secuencia  para el próximo nivel
                    mostrarPatronSimon()
                }
            }
        } else {

            Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
            reiniciarJuegoSimonDice()
        }
    }




}