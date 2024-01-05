package com.example.mygardenrgr

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mygardenrgr.databinding.ActivityUsoStorage2Binding
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream

class UsoStorage2 : AppCompatActivity() {

    private lateinit var binding: ActivityUsoStorage2Binding
    private var imagenAgregada = false
    private val TAG = "RGRPC"

    // Instancia de Firebase Storage
    private val storage = Firebase.storage
    private val storageRef = storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsoStorage2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("email")

        // Referencia a la carpeta imgUsers
        val imagesRef: StorageReference = storageRef.child("imgUsers")

        // Listar imágenes en la carpeta imgUsers
        imagesRef.listAll()
            .addOnSuccessListener {
                Log.d(TAG, "Items en imgUsers")
                for (item in it.items) {
                    Log.e(TAG, item.toString())
                }
            }
            .addOnFailureListener {
                // Manejar error si es necesario
                Log.e(TAG, "Error al listar imágenes en imgUsers")
            }

        binding.btCargar1.setOnClickListener {
            fileUpload()
        }

        binding.atras1.setOnClickListener {
            if (imagenAgregada) {
                finish() // Cierra la actividad solo si se ha añadido una imagen
            } else {
                Toast.makeText(this, "Debes añadir una imagen primero", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Activity para lanzar la galería de imágenes.
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d(TAG, "Selected URI: $uri")
            binding.imagenUser.setImageURI(uri)
            Log.d(TAG, "Cargada")
            val Folder: StorageReference =
                FirebaseStorage.getInstance().getReference().child("imgUsers")
            val email = intent.getStringExtra("email")
            val file_name: StorageReference = Folder.child(email.toString())
            file_name.putFile(uri).addOnSuccessListener { taskSnapshot ->
                file_name.downloadUrl.addOnSuccessListener { uri ->
                    Toast.makeText(this,"Imagen ${uri.toString()} subida correctamente", Toast.LENGTH_SHORT).show()
                    imagenAgregada = true
                }
            }
        } else {
            Toast.makeText(this, "Debes seleccionar una imagen primero", Toast.LENGTH_SHORT).show()
            imagenAgregada = false
        }
    }

    fun fileUpload() {
        val email = intent.getStringExtra("email")
        var nomImagen=email.toString()
        if(nomImagen==""){
            Toast.makeText(this,"Introduce un nombre para la imagen", Toast.LENGTH_SHORT).show()
        }
        else {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    // Agrega esta función a tu clase
    private fun uploadImageToFirebaseStorage(bitmap: Bitmap) {
        val email = intent.getStringExtra("email")
        val nomImagen = email.toString()

        if (nomImagen.isNotEmpty()) {
            val storageRef = FirebaseStorage.getInstance().getReference().child("imgUsers/$nomImagen")
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()

            storageRef.putBytes(byteArray)
                .addOnSuccessListener { taskSnapshot ->
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        Toast.makeText(this, "Imagen subida correctamente: ${uri.toString()}", Toast.LENGTH_SHORT).show()
                        imagenAgregada = true
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error al subir la imagen: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Introduce un nombre para la imagen", Toast.LENGTH_SHORT).show()
        }
    }
}
