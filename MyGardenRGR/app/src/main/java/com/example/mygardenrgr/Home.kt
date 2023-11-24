package com.example.mygardenrgr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import android.app.AlertDialog
import android.content.Intent
import com.example.conexionyloginantonio.User
import com.example.mygardenrgr.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {



    lateinit var binding: ActivityHomeBinding
    private lateinit var firebaseauth : FirebaseAuth
    val TAG = "ACSCO"
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Para la autenticación, de cualquier tipo.
        firebaseauth = FirebaseAuth.getInstance()

        //Recuperamos los datos del login.
        binding.txtEmail.text = intent.getStringExtra("email").toString()
        binding.txtProveedor.text = intent.getStringExtra("provider").toString()
        binding.txtNombre.text = intent.getStringExtra("nombre").toString()

        binding.btCerrarSesion.setOnClickListener {
            Log.e(TAG, firebaseauth.currentUser.toString())
            // Olvidar al usuario, limpiando cualquier referencia persistente
            //comprobadlo en Firebase, como ha desaparecido.
//            firebaseauth.currentUser?.delete()?.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    firebaseauth.signOut()
//                    Log.e(TAG,"Cerrada sesión completamente")
//                } else {
//                    Log.e(TAG,"Hubo algún error al cerrar la sesión")
//                }
//            }
            firebaseauth.signOut()

            val signInClient = Identity.getSignInClient(this)
            signInClient.signOut()
            Log.e(TAG,"Cerrada sesión completamente")
            finish()

        }
        binding.btVolver.setOnClickListener {
            irAVentanaPrincipal(intent.getStringExtra("email").toString())
        }
        binding.btGuardar.setOnClickListener {
            val userEmail = binding.txtEmail.text.toString()
            val userRole = if (userEmail == "admin@gmail.com") Rol.ADMIN else Rol.BASIC

            // Se guardarán en modo HashMap (clave, valor).
            val user = hashMapOf(
                "provider" to binding.txtProveedor.text,
                "email" to userEmail,
                "name" to binding.edNombre.text.toString(),
                "age" to binding.edEdad.text.toString(),
                "roles" to userRole.name, // Asigna directamente el nombre del rol como String
                "timestamp" to FieldValue.serverTimestamp()
            )

            // Si no existe el documento lo crea, si existe lo remplaza.
            db.collection("users")
                .document(userEmail) // Será la clave del documento.
                .set(user).addOnSuccessListener {
                    Toast.makeText(this, "Almacenado", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                }

        }

        binding.btEliminar.setOnClickListener {
            //Buscamos antes si existe un campo con ese email en un documento.
            val id = db.collection("users")
                .whereEqualTo("email",binding.txtEmail.text.toString())
                .get()
                .addOnSuccessListener {result ->
                    //En result, vienen los que cumplen la condición (si no pongo nada es it)
                    //Con esto borramos el primero.
                    //db.collection("users").document(result.elementAt(0).id).delete().toString()
                    //Con esto borramos todos. No olvidar que id aquí no es una Primarykey, puede repetirse.
                    for (document in result) {
                        db.collection("users")
                            .document(document.id)
                            .delete().toString() //lo importante aquí es el delete. el toString es pq además devuelve un mensaje con lo sucedido.
                    }

                    Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(this, "No se ha encontrado el documento a eliminar", Toast.LENGTH_SHORT).show()
                }

        }
        binding.btRecuperar.setOnClickListener {
            mostrarTextViews()
            //Búsqueda por id del documento.
            db.collection("users")
                .document(binding.txtEmail.text.toString())
                .get()
                .addOnSuccessListener {
                    //Si encuentra el documento será satisfactorio este listener y entraremos en él.
                    binding.edNombre.setText(it.get("name") as String?)
                    binding.edEdad.setText(it.get("age") as String?)

                    Toast.makeText(this, "Recuperado", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Algo ha ido mal al recuperar", Toast.LENGTH_SHORT).show()
                }

        }




        // Mostrar el diálogo al entrar en la ventana Home
        mostrarDialogoIrDatosPersonales()
    }//OVERRIDE


    // Función para mostrar el diálogo
    private fun mostrarDialogoIrDatosPersonales() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ir al apartado de datos personales")
        builder.setMessage("¿Desea ir al apartado de datos personales?")
        builder.setPositiveButton("Sí") { _, _ ->
            // Si la respuesta es sí, mostrar elementos relacionados con datos personales
            mostrarDatosPersonales()
        }
        builder.setNegativeButton("No") { _, _ ->



        }
        builder.show()
    }

    // Función para mostrar elementos relacionados con datos personales
    private fun mostrarDatosPersonales() {
        binding.edNombre.visibility = View.VISIBLE
        binding.edEdad.visibility = View.VISIBLE
        binding.btGuardar.visibility = View.VISIBLE
        binding.btEliminar.visibility = View.VISIBLE
        binding.btRecuperar.visibility = View.VISIBLE

        // Ocultar elementos específicos
        binding.txtEmail.visibility = View.GONE
        binding.txtProveedor.visibility = View.GONE
        binding.txtNombre.visibility = View.GONE
    }
    private fun ocultarDatosPersonales() {
        binding.edNombre.visibility = View.GONE
        binding.edEdad.visibility = View.GONE
        binding.btGuardar.visibility = View.GONE
        binding.btEliminar.visibility = View.GONE
        binding.btRecuperar.visibility = View.GONE

        // Ocultar elementos específicos
        binding.txtEmail.visibility = View.GONE
        binding.txtProveedor.visibility = View.GONE
        binding.txtNombre.visibility = View.GONE
    }

    // Función para mostrar TextViews
    private fun mostrarTextViews() {
        binding.txtEmail.visibility = View.VISIBLE
        binding.txtProveedor.visibility = View.VISIBLE
        binding.txtNombre.visibility = View.VISIBLE
    }

    // Función para ir a la VentanaPrincipal
    private fun irAVentanaPrincipal(email: String) {
        ocultarDatosPersonales()
        val intent = Intent(this, VentanaPrincipal::class.java).apply {
            putExtra("email", email)
            putExtra("provider", intent.getStringExtra("provider"))
            putExtra("nombre", intent.getStringExtra("nombre"))
        }
        startActivity(intent)
        finish()  // Cierra la actividad actual para que no puedas volver atrás
    }



}