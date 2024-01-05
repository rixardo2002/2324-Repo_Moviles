package com.example.mygardenrgr

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mygardenrgr.databinding.ActivityUsoStorageBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class UsoStorage : AppCompatActivity() {
    private lateinit var binding: ActivityUsoStorageBinding
    private var imagenAgregada = false
    var storage = Firebase.storage
    // Crea una referencia con la instancia singleton FirebaseStorage y con una llamada al método reference.
    var storageRef = storage.reference

    private val cameraRequest = 1888
    private lateinit var bitmap : Bitmap
    var uriImagen : Uri? = null
    val TAG  = "RGRPC"

    //Segunda activity para lanzar la cámara.
    // Modifica la función openCamera
    val openCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data!!
            this.bitmap = data.extras!!.get("data") as Bitmap
            binding.imagenPerro.setImageBitmap(bitmap)

            // Llama a la función para cargar la imagen en Firebase Storage
            uploadImageToFirebaseStorage(bitmap)
        }
    }

    //Activity para pedir permisos de cámara.
    val requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //si quisieras vídeo. Poner el punto y ver resto de opciones que ofrece, que prueben alguna.
            //val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            openCamera.launch(intent)
        } else {
            Log.e(TAG,"Permiso de cámara no concedido")
        }
    }







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_uso_storage)
        binding = ActivityUsoStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtén el ID de la intención
        val id = intent.getStringExtra("id")


        // Create a child reference
        // imagesRef now points to "images"
        var imagesRef: StorageReference? = storageRef.child("images")

        // Child references can also take paths
        // spaceRef now points to "images/space.jpg
        // imagesRef still points to "images"
        var spaceRef = storageRef.child("images/spock.jpg") //spock.jpg es el nombre de la imagen


        // parent allows us to move our reference to a parent node
        // imagesRef now points to 'images'
        imagesRef = spaceRef.parent
        Log.e(TAG, imagesRef!!.path.toString())

        // root allows us to move all the way back to the top of our bucket
        // rootRef now points to the root
        val rootRef = spaceRef.root
        Log.e(TAG, rootRef.path.toString())



        // File path is "images/spock.jpg"
        val path = spaceRef.path
        Log.e(TAG, path.toString())

        // File name is "spock.jpg"
        val name = spaceRef.name
        Log.e(TAG, name.toString())

        //Listar todos.
        val listRef = storage.reference.child("images")
        listRef.listAll()
            .addOnSuccessListener {
                Log.d(TAG,"Items en images")
                for (item in it.items) {
                    Log.e(TAG,item.toString())
                }
            }
            .addOnFailureListener {
                // Uh-oh, an error occurred!
            }

        binding.btCargar.setOnClickListener {
            fileUpload()
        }
        binding.openCamera.setOnClickListener {

            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        binding.atras.setOnClickListener {
            if (imagenAgregada) {
                finish() // Cierra la actividad solo si se ha añadido una imagen
            } else {
                Toast.makeText(this, "Debes añadir una imagen primero", Toast.LENGTH_SHORT).show()
            }
        }

    }//FIN OVERRIDE

    /**
     * Subida de imagen. Seleccionamos de la galería de imágenes (ver ejemplo de cargar Fotos). Si queremos subir de la cámara ver ejemplo Fotos.
     */
    //------------------------- Funciones de callback para activities results -------------------------
    //Activity para lanzar la galería de imágenes.
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d(TAG, "Selected URI: $uri")
            binding.imagenPerro.setImageURI(uri)
            Log.d(TAG, "Cargada")
            val Folder: StorageReference =
                FirebaseStorage.getInstance().getReference().child("images")
            val id = intent.getStringExtra("id")
            val file_name: StorageReference = Folder.child(id.toString())
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
        val id = intent.getStringExtra("id")
        var nomImagen=id.toString()
        if(nomImagen==""){
            Toast.makeText(this,"Introduce un nombre para la imagen", Toast.LENGTH_SHORT).show()
        }
        else {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    // Agrega esta función a tu clase
    private fun uploadImageToFirebaseStorage(bitmap: Bitmap) {
        val id = intent.getStringExtra("id")
        val nomImagen = id.toString()

        if (nomImagen.isNotEmpty()) {
            val storageRef = FirebaseStorage.getInstance().getReference().child("images/$nomImagen")
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