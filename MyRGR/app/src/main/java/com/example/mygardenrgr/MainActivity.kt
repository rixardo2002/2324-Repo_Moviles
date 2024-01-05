package com.example.mygardenrgr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.loginconexion.Proveedor
import com.example.mygardenrgr.databinding.ActivityMainBinding
import com.example.mygardenrgr.databinding.DialogOpcionesBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding
    private lateinit var firebaseauth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    val TAG = "ACSCO"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Para la autenticación, de cualquier tipo.
        firebaseauth = FirebaseAuth.getInstance()

        // Configura un OnClickListener para el botón de opciones
        binding.btOpciones.setOnClickListener {
            mostrarDialogoOpciones()
        }


        //------------------------------ Autenticación con email y password ------------------------------------
//        binding.btRegistrar.setOnClickListener {
//            if (binding.edEmail.text!!.isNotEmpty() && binding.edPass.text!!.isNotEmpty()){
//                firebaseauth.createUserWithEmailAndPassword(binding.edEmail.text.toString(),binding.edPass.text.toString()).addOnCompleteListener {
//                    if (it.isSuccessful){
//                        irVentanaPrincipal(it.result?.user?.email?:"", Proveedor.EMAIL)  //Esto de los interrogantes es por si está vacío el email, que enviaría una cadena vacía.
//                    } else {
//                        showAlert(binding.edEmail.text.toString(),binding.edPass.text.toString())
//                    }
//                }.addOnFailureListener{
//                    Toast.makeText(this, R.string.conexion, Toast.LENGTH_SHORT).show()
//                }
//            }
//            else {
//                showAlert(binding.edEmail.text.toString(),binding.edPass.text.toString())
//            }
//        }

//        binding.btLogin.setOnClickListener {
//            if (binding.edEmail.text!!.isNotEmpty() && binding.edPass.text!!.isNotEmpty()){
//                firebaseauth.signInWithEmailAndPassword(binding.edEmail.text.toString(),binding.edPass.text.toString()).addOnCompleteListener {
//                    if (it.isSuccessful){
//                        irVentanaPrincipal(it.result?.user?.email?:"", Proveedor.EMAIL)  //Esto de los interrogantes es por si está vacío el email.
//                    } else {
//                        showAlert(binding.edEmail.text.toString(),binding.edPass.text.toString())
//                    }
//                }.addOnFailureListener{
//                    Toast.makeText(this, R.string.conexion, Toast.LENGTH_SHORT).show()
//                }
//            }
//            else {
//                showAlert(binding.edEmail.text.toString(),binding.edPass.text.toString())
//            }
//        }

//        //------------------ Login Google -------------------
//        //------------------------------- -Autenticación Google --------------------------------------------------
//        //se hace un signOut por si había algún login antes.
//        firebaseauth.signOut()
//        //clearGooglePlayServicesCache()
//
//        //esta variable me conecta con  google. y todo este bloque prepara la ventana de google que se destripa en el loginInGoogle
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.your_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(this,gso)

//        binding.btGoogle.setOnClickListener {
//            loginEnGoogle()
//        }
    }//OVERRIDE
    //******************************* Para el login con Google ******************************
    //--------
    private fun loginEnGoogle(){
        //este método es nuestro.
        val signInClient = googleSignInClient.signInIntent
        launcherVentanaGoogle.launch(signInClient)
        //milauncherVentanaGoogle.launch(signInClient)
    }


    //con este launcher, abro la ventana que me lleva a la validacion de Google.
    private val launcherVentanaGoogle =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        //si la ventana va bien, se accede a las propiedades que trae la propia ventana q llamamos y recogemos en result.
        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            manejarResultados(task)
        }
    }

    //es como una muñeca rusa, vamos desgranando, de la ventana a task y de task a los datos concretos que me da google.
    private fun manejarResultados(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                actualizarUI(account)
            }
        }
        else {
            Toast.makeText(this,task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    //esta funcion actualiza o repinta la interfaz de usuario UI.
    private fun actualizarUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        //pido un token, y con ese token, si todo va bien obtengo la info.
        firebaseauth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                //hacer account. y ver otras propiedades interesantes.
                irVentanaPrincipal(account.email.toString(),Proveedor.GOOGLE, account.displayName.toString())
            }
            else {
                Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    //************************************** Funciones auxiliares **************************************
    //*********************************************************************************

    private fun mostrarDialogoOpciones() {
        // Crea un cuadro de diálogo
        val builder = AlertDialog.Builder(this)
        val dialogBinding = DialogOpcionesBinding.inflate(LayoutInflater.from(this))

        builder.setView(dialogBinding.root)

        val dialog = builder.create()
        //------------------ Login Google -------------------
        //------------------------------- -Autenticación Google --------------------------------------------------
        //se hace un signOut por si había algún login antes.
        firebaseauth.signOut()
        //clearGooglePlayServicesCache()

        //esta variable me conecta con  google. y todo este bloque prepara la ventana de google que se destripa en el loginInGoogle
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)
        // Configura acciones para los botones dentro del diálogo
        dialogBinding.btGoogle.setOnClickListener {
            loginEnGoogle()
            dialog.dismiss() // Cerrar el diálogo después de la acción
        }

        dialogBinding.btLogin.setOnClickListener {
            if (binding.edEmail.text!!.isNotEmpty() && binding.edPass.text!!.isNotEmpty()){
                firebaseauth.signInWithEmailAndPassword(binding.edEmail.text.toString(),binding.edPass.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        irVentanaPrincipal(it.result?.user?.email?:"", Proveedor.EMAIL)  //Esto de los interrogantes es por si está vacío el email.
                    } else {
                        showAlert(binding.edEmail.text.toString(),binding.edPass.text.toString())
                    }
                }.addOnFailureListener{
                    Toast.makeText(this, R.string.conexion, Toast.LENGTH_SHORT).show()
                }
            }
            else {
                showAlert(binding.edEmail.text.toString(),binding.edPass.text.toString())
            }
            dialog.dismiss() // Cerrar el diálogo después de la acción
        }

        dialogBinding.btRegistrar.setOnClickListener {
            if (binding.edEmail.text!!.isNotEmpty() && binding.edPass.text!!.isNotEmpty()){
                firebaseauth.createUserWithEmailAndPassword(binding.edEmail.text.toString(),binding.edPass.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        irVentanaPrincipal(it.result?.user?.email?:"", Proveedor.EMAIL)  //Esto de los interrogantes es por si está vacío el email, que enviaría una cadena vacía.
                    } else {
                        showAlert(binding.edEmail.text.toString(),binding.edPass.text.toString())
                    }
                }.addOnFailureListener{
                    Toast.makeText(this, R.string.conexion, Toast.LENGTH_SHORT).show()
                }
            }
            else {
                showAlert(binding.edEmail.text.toString(),binding.edPass.text.toString())
            }
            dialog.dismiss() // Cerrar el diálogo después de la acción
        }

        // Muestra el diálogo
        dialog.show()
    }
    private fun showAlert(email: String, password: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.error)

        when {
            email.isEmpty() || password.isEmpty() -> {
                builder.setMessage(R.string.campos)
            }
            password.length < 8 -> {
                builder.setMessage(R.string.valicontra)
            }
            else -> {
                builder.setMessage(R.string.incorrectoUsuContra)
            }
        }

        builder.setPositiveButton(R.string.aceptar, null)
        val dialog: AlertDialog = builder.create()

        if (password.length < 8) {
            // Si la contraseña es menor a 8 caracteres, no permitir que el usuario continúe.
            dialog.setCancelable(false)
        }

        dialog.show()
    }



    //*********************************************************************************
    private fun irVentanaPrincipal(email:String, provider:Proveedor, nombre:String = "Usuario"){
        Log.e(TAG,"Valores: ${email}, ${provider}, ${nombre}")

        val homeIntent = Intent(this, VentanaPrincipal::class.java).apply {
            putExtra("email",email)
            putExtra("provider",provider.name)
            putExtra("nombre",nombre)

        }
        startActivity(homeIntent)
    }


}