package com.example.mygardenrgr

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mygardenrgr.databinding.ActivityVentanaDetalleBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException

class VentanaDetalle : AppCompatActivity() {

    lateinit var binding: ActivityVentanaDetalleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityVentanaDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var p  = intent.getSerializableExtra("obj") as Producto
        val rol = intent.getStringExtra("rol")!!
        Log.d("RGRPC", "${rol.toLowerCase()}")
        val emailCli = intent.getStringExtra("emailCli")!!
        Log.d("RGRPC", "${emailCli.toLowerCase()}")
        var nombre=p.nombre
        var procedencia=p.raza
        var id=p.id
        var cantidad=p.edad
        //var contenido=contenidoDescripcion(nombre)

        binding.textName.text=nombre
        binding.textPlace.text=procedencia
        binding.textCant.text=cantidad

        // Aquí obtienes la referencia a Firebase Storage
        val storage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = storage.reference

        // Aquí construyes la referencia a la imagen en Storage
        val imgPerfil = p.id // Asegúrate de tener esta propiedad en TuTipoDeDatos
        Log.e(ContentValues.TAG,"${p.id}")

        val refFoto: StorageReference = storageRef.child("images/$imgPerfil")

        // Aquí descargas la imagen y la cargas en el ImageView
        try {
            val localFile = File.createTempFile("tempImage", "jpg")
            refFoto.getFile(localFile).addOnSuccessListener {
                val bitmap: Bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                binding.imageView.setImageBitmap(bitmap)

            }.addOnFailureListener {
                Log.e("TuAdaptador", "Fallo al cargar la foto del usuario")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (rol == "CLIENTE") {
            // Si el rol es cliente, hacemos visible el botón
            binding.contactoPerro.visibility = View.VISIBLE

            binding.contactoPerro.setOnClickListener{
                enviarCorreo(emailCli,p)
            }
        } else {
            // Si el rol no es cliente, hacemos invisible el botón
            binding.contactoPerro.visibility = View.INVISIBLE
        }


        binding.btnVolver.setOnClickListener {
            finish()
        }
        // Agregar OnClickListener al botón
        binding.verMasButton.setOnClickListener {

            mostrarDialogoDescripcion(p.descripcion)
        }
    }//override



    private fun enviarCorreo(email:String,p:Producto) {
        val email = email
        val subject = "Solicitud para adopción"
        val message = "Querria ponerme en contacto para poder solicitar a: Nombre:${p.nombre}, RAZA${p.raza},EDAD: ${p.edad}"

        enviarCorreoConDatos(subject, message)
    }

    private fun enviarCorreoConDatos( subject: String, message: String) {
        val email="casaperrosr@gmail.com"
        val uri = Uri.parse("mailto:$email?subject=$subject&body=$message")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        startActivity(intent)
    }

    // Función para mostrar un cuadro de diálogo con la descripción completa
    private fun mostrarDialogoDescripcion(descripcion: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_descripcion, null)

        val textDescripcion = dialogView.findViewById<TextView>(R.id.textDescripcion)
        val btnCerrar = dialogView.findViewById<Button>(R.id.btnCerrar)

        textDescripcion.text = descripcion

        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()

        btnCerrar.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }




}