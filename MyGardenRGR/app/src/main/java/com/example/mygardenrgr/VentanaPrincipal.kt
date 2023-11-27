package com.example.mygardenrgr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.mygardenrgr.databinding.ActivityVentanaPrincipalBinding

class VentanaPrincipal : AppCompatActivity() {

    lateinit var binding:ActivityVentanaPrincipalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentanaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener los valores del Intent
        val email = intent.getStringExtra("email").toString()


        binding.toolbarPrincipal.title = "My Garden"
        binding.toolbarPrincipal.subtitle = email ?: ""


        //aquí simplemente inflo la toolBaar, pero aún no hay opciones ni botón home.
        setSupportActionBar(binding.toolbarPrincipal)


        //en las siguientes líneas hago que aparezca el botón de atrás (home) y genero su evento
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarPrincipal.setNavigationOnClickListener {
            Toast.makeText(this,"Pulsado el retroceso", Toast.LENGTH_SHORT).show()
        }

        binding.imgBttnHuerto.setOnClickListener(){
            // Crear un Intent para cambiar a la actividad VentanaRecycler
            val intent = Intent(this, VentanaLista::class.java)

            // Agregar el correo electrónico como un extra al Intent
            intent.putExtra("email", email)

            // Iniciar la nueva actividad
            startActivity(intent)

        }






    }//OVERRIDE

    //************************* Funciones auxiliares para los menú de puntos *****************************
    //la primera infla el menú que previamente hemos creado como resource, la segunda carga las opciones.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnPerson -> {
                // Aquí lanzas la actividad Home y le pasas los datos
                val intent = Intent(this, Home::class.java).apply {
                    putExtra("email", intent.getStringExtra("email"))
                    putExtra("provider", intent.getStringExtra("provider"))
                    putExtra("nombre", intent.getStringExtra("nombre"))
                }
                startActivity(intent)
                finish()
            }
            R.id.mnOp1 -> {
                val miIntent = Intent(this, VentanaOpcion1::class.java)
                miIntent.putExtra("Email", intent.getStringExtra("email").toString())
                startActivity(miIntent)
            }
            R.id.mnOp2 -> {
                //irAOpcion2()
            }
            R.id.mnOp3 -> {
                //irAOpcion3()
            }
            R.id.mnOp4 -> {
                //irAOpcion4()
            }

        }
        return super.onOptionsItemSelected(item)
    }



}