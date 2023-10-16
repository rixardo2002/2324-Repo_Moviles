package com.example.formulariopasodatos_rgr

import Usuario
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.formulariopasodatos_rgr.databinding.ConfirmationActivityBinding



class ConfirmationActivity : AppCompatActivity() {
    lateinit var binding: ConfirmationActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ConfirmationActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar el usuario y la lista de usuarios de la intención
        val usuario = intent.getSerializableExtra("usuario") as? Usuario
        val listaUsuarios = intent.getSerializableExtra("listaUsuarios") as? ArrayList<Usuario>

        // Verificar si el usuario y la lista de usuarios no son nulos
        if (usuario != null && listaUsuarios != null) {
            // Mostrar los datos del usuario en los TextView
            binding.textView1.text = usuario.nombre
            binding.apellido.text = usuario.apellido
            binding.dni.text = usuario.DNI
            binding.gmail.text = usuario.gmail
            binding.contrasenia.text = usuario.contrasenia

            // Mostrar la lista de usuarios en el MultiLineText
            val listaUsuariosText = StringBuilder()
            for (user in listaUsuarios) {
                listaUsuariosText.append(user.toString()).append("\n")
            }
            binding.editTextTextMultiLine.setText(listaUsuariosText.toString())
        }
        binding.button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            limpiarCampos()
            finish()
        }

    }
    private fun limpiarCampos() {
        binding.textView1.text = ""
        binding.apellido.text = ""
        binding.dni.text = ""
        binding.gmail.text = ""
        binding.contrasenia.text = ""
        binding.editTextTextMultiLine.setText("")

    }
}












