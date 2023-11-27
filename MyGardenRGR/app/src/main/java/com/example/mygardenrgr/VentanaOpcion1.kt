package com.example.mygardenrgr

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class VentanaOpcion1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventana_opcion1)

        val btnAbrirEnlace1: Button = findViewById(R.id.btnAbrirEnlace1)
        val btnAbrirEnlace2: Button = findViewById(R.id.btnAbrirEnlace2)
        val btnEnviarCorreo: Button = findViewById(R.id.btnEnviarCorreo)

        btnAbrirEnlace1.setOnClickListener {
            abrirEnlace1()
        }

        btnAbrirEnlace2.setOnClickListener {
            abrirEnlace2()
        }

        btnEnviarCorreo.setOnClickListener {
            enviarCorreo()
        }
    }//Override

    private fun abrirEnlace1() {
        val url = "https://github.com/rixardo2002/2324-Repo_Moviles"
        abrirEnlace(url)
    }

    private fun abrirEnlace2() {
        val url = "https://www.lahuertinadetoni.es/huerta-para-principiantes/"
        abrirEnlace(url)
    }

    private fun abrirEnlace(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun enviarCorreo() {
        val email = "ricardogomezramos@gmail.com"
        val subject = "Asunto del correo"
        val message = "Contenido del mensaje"

        enviarCorreoConDatos(email, subject, message)
    }

    private fun enviarCorreoConDatos(email: String, subject: String, message: String) {
        val uri = Uri.parse("mailto:$email?subject=$subject&body=$message")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        startActivity(intent)
    }

}