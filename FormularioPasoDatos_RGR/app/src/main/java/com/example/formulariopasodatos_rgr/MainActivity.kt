package com.example.formulariopasodatos_rgr

import Usuario
import AlmacenUsuarios
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.formulariopasodatos_rgr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var usuario: Usuario
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

                binding.bttnRegistro.setOnClickListener() {

                    var nombree = binding.txtPlainN.text.toString()
                    var apellido = binding.cajaApellido.text.toString()
                    var dni = binding.cajaDNI.text.toString()
                    var gmail = binding.cajaGmail.text.toString()
                    var contrasenia = binding.cajaContrasenia.text.toString()

                    usuario = Usuario(nombree, apellido, dni, gmail, contrasenia)

                    if (validarCampos(usuario.nombre,usuario.apellido,
                            usuario.DNI,usuario.gmail,usuario.contrasenia)) {

                        AlmacenUsuarios.aniadirPersona(usuario)

                        limpiarCampos()
                        irConfirmationActivity(usuario)
                    }
                }
    }
    private fun irConfirmationActivity(usuario: Usuario) {
            val miIntent = Intent(this, ConfirmationActivity::class.java)

            miIntent.putExtra("usuario",usuario)
            miIntent.putExtra("listaUsuarios", AlmacenUsuarios.usuarios)

            startActivity(miIntent)

    }


    private fun validarCampos(nombre: String, apellido: String, dni: String, gmail: String, contrasenia: String): Boolean {
        var confirmacionCont = binding.cajaConfirmacion.text.toString()
        if (nombre.isBlank() || apellido.isBlank() || dni.isBlank() || gmail.isBlank() || contrasenia.isBlank() || confirmacionCont.isBlank()) {
            Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_SHORT).show()
            return false

        } else if (contrasenia.length < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return false

        } else if (contrasenia != confirmacionCont) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return false

        } else if (existeDNI(dni,AlmacenUsuarios.usuarios)) {
            Toast.makeText(this, "Este DNI ya está registrado", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
    fun existeDNI(dni: String,listaUsu: ArrayList<Usuario>): Boolean {

        for (usuario in listaUsu) {
            if (usuario.DNI == dni) {
                return true
            }
        }
        return false
    }
    private fun limpiarCampos() {
        binding.txtPlainN.text.clear()
        binding.cajaApellido.text.clear()
        binding.cajaDNI.text.clear()
        binding.cajaGmail.text.clear()
        binding.cajaContrasenia.text.clear()
        binding.cajaConfirmacion.text.clear()
    }

}


