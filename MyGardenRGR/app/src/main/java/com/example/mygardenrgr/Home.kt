package com.example.mygardenrgr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.conexionyloginantonio.User
import com.example.mygardenrgr.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {


    /**************************DATOS DE PRUEBA ******************************************************************/
    var miArray:ArrayList<User> = ArrayList()  //Este será el arrayList que se usará para el adapter del RecyclerView o de la ListView.
    //Valores fake.
    val nombres = listOf("Ragnar","Ivar","Lagertha","Floki")
    val apellidos = listOf("Lothbrok","Sin huesos","Piel de Hierro","Semi diosa")
    val edades = listOf(18, 23, 45, 67, 34, 47, 41)
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
            // Log.e(TAG, firebaseauth.currentUser.toString())
            firebaseauth.signOut()
            finish()
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





    }//OVERRIDE
}