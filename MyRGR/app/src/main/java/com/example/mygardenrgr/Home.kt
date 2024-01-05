package com.example.mygardenrgr

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.mygardenrgr.databinding.ActivityHomeBinding
import com.example.mygardenrgr.databinding.DialogRegistroBinding
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException

class Home : AppCompatActivity() {

    private val canalNombre = "not-RGR"
    private val canalId = "canalId"
    private val notificacionId = 0
    lateinit var binding: ActivityHomeBinding
    private lateinit var firebaseauth: FirebaseAuth
    val TAG = "RGRPC"
    private var emailUsuario: String = ""
    val db = Firebase.firestore
    private var numeroTelefono: String = ""
    private var emailFoto: String = ""
    companion object {
        const val REQUEST_SMS_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Para la autenticación, de cualquier tipo.
        firebaseauth = FirebaseAuth.getInstance()

        //Recuperamos los datos del login.
        val email = intent.getStringExtra("email").toString()
        this.emailFoto=email
        binding.txtEmail.text = intent.getStringExtra("email").toString()

        binding.btnEdit.setOnClickListener {

            abrirVentanaUsoStorage2(email)

        }
        // Aquí obtienes la referencia a Firebase Storage
        val storage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = storage.reference

        // Aquí construyes la referencia a la imagen en Storage
        val imgPerfil = email// Asegúrate de tener esta propiedad en TuTipoDeDatos
        Log.e(TAG,"${email}")
        val refFoto: StorageReference = storageRef.child("imgUsers/$imgPerfil")
        try {
            val localFile = File.createTempFile("tempImage", "jpg")
            refFoto.getFile(localFile).addOnSuccessListener {
                val bitmap: Bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                // Asigna el bitmap a tu CircleImageView
                binding.circleImageView.setImageBitmap(bitmap)
            }.addOnFailureListener {
                Log.e("TuAdaptador", "Fallo al cargar la foto del usuario")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }


        binding.btnInscribirse.setOnClickListener {
            mostrarDialogoRegistro()
        }

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
            crearCanalNotificacion()
            crearNotificacion(email)
            Log.e(TAG, "Cerrada sesión completamente")
            finish()

        }
        binding.btVolver.setOnClickListener {
            irAVentanaPrincipal(intent.getStringExtra("email").toString())
        }


        binding.btEliminar.setOnClickListener {
            // Crear un AlertDialog.Builder
            val builder = AlertDialog.Builder(this)

            // Configurar el mensaje del cuadro de diálogo
            builder.setMessage(getString(R.string.confirmareliminacion))

            // Configurar el botón "Sí"
            builder.setPositiveButton(R.string.si) { _, _ ->
                // Buscamos antes si existe un campo con ese email en un documento.
                val id = db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {

                            val numeroTelefono = document.getString("telefono") ?: ""
                            this.numeroTelefono=numeroTelefono
                            // Verificar y solicitar permisos en tiempo de ejecución
                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), REQUEST_SMS_PERMISSION)
                            } else {
                                // Ya se tiene el permiso, puedes enviar el SMS
                                enviarSMS(numeroTelefono)
                            }
                            Log.d("RGRPC", "Número de teléfono antes de enviar SMS: $numeroTelefono")



                            db.collection("users")
                                .document(document.id)
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(this, R.string.eliminado, Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, R.string.noencontradodocumento, Toast.LENGTH_SHORT).show()
                    }
            }

            // Configurar el botón "No"
            builder.setNegativeButton(R.string.no) { _, _ ->
                // No hacer nada, simplemente cerrar el cuadro de diálogo
            }

            // Mostrar el cuadro de diálogo
            builder.create().show()
        }


        binding.btRecuperar.setOnClickListener {
            // Búsqueda por id del documento.
            db.collection("users")
                .document(binding.txtEmail.text.toString())
                .get()
                .addOnSuccessListener { document ->
                    // Si encuentra el documento será satisfactorio este listener y entraremos en él.
                    val nombre = document.get("name") as String?
                    val edad = document.get("age") as String?
                    val rol = document.get("rol") as String? ?: ""
                    val genero = document.get("genero") as String? ?: ""
                    val telf = document.get("telefono") as String?
                    Log.e(TAG, nombre.toString())

                    // Mostrar el diálogo con la información recuperada
                    mostrarDialogRecuperarDatos(
                        binding.txtEmail.text.toString(),
                        nombre ?: "",
                        edad ?: "",
                        rol,
                        genero,
                        telf ?: ""
                    )

                    Toast.makeText(this, R.string.recuperado, Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, R.string.idomal, Toast.LENGTH_SHORT).show()
                }
        }


    }//OVERRIDE
    override fun onResume() {
        super.onResume()
        cargarImagenDesdeStorage(emailFoto)
    }


    private fun cargarImagenDesdeStorage(email:String) {
        val storage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = storage.reference
        val imgPerfil = email

        val refFoto: StorageReference = storageRef.child("imgUsers/$imgPerfil")

        try {
            val localFile = File.createTempFile("tempImage", "jpg")
            refFoto.getFile(localFile).addOnSuccessListener {
                val bitmap: Bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                binding.circleImageView.setImageBitmap(bitmap)

            }.addOnFailureListener {
                Log.e("TuAdaptador", "Fallo al cargar la foto del usuario")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }



    private fun mostrarDialogoRegistro() {
        val dialogRegistroBinding = DialogRegistroBinding.inflate(LayoutInflater.from(this))
        val builder = AlertDialog.Builder(this).setView(dialogRegistroBinding.root)

        val dialog = builder.create()

        // Configura acciones para el botón "Guardar" del cuadro de diálogo
        dialogRegistroBinding.btnGuardar.setOnClickListener {
            // Obtiene los valores del cuadro de diálogo
            val userEmail = binding.txtEmail.text.toString()

            // Obtiene el tipo (cliente o trabajador)
            val tipoSeleccionado =
                when (dialogRegistroBinding.radioGroupTipo.checkedRadioButtonId) {
                    R.id.rbCliente -> Rol.CLIENTE
                    R.id.rbTrabajador -> Rol.TRABAJADOR
                    else -> Rol.CLIENTE // Valor predeterminado en caso de que no se seleccione ninguno
                }

            // Obtiene el sexo
            val sexoSeleccionado =
                when (dialogRegistroBinding.radioGroupSexo.checkedRadioButtonId) {
                    R.id.rbMasculino -> "Masculino"
                    R.id.rbFemenino -> "Femenino"
                    else -> "Masculino" // Puedes manejar un valor predeterminado o error aquí
                }

            // Verifica si todos los campos están llenos
            if (userEmail.isNotEmpty() &&

                dialogRegistroBinding.edNombre.text!!.isNotEmpty() &&
                dialogRegistroBinding.edEdad.text!!.isNotEmpty() &&
                dialogRegistroBinding.edTelefono.text!!.isNotEmpty()
            ) {
                // Crea un HashMap con la información del usuario
                val user = hashMapOf(
                    "email" to userEmail,
                    "name" to dialogRegistroBinding.edNombre.text.toString(),
                    "rol" to tipoSeleccionado,
                    "genero" to sexoSeleccionado,
                    "age" to dialogRegistroBinding.edEdad.text.toString(),
                    "telefono" to dialogRegistroBinding.edTelefono.text.toString(),
                    "timestamp" to FieldValue.serverTimestamp()
                )

                // Guarda la información en Firestore
                db.collection("users")
                    .document(userEmail)
                    .set(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, R.string.almacenado, Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, R.string.ocurridoerror, Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
            } else {
                // Muestra un Toast indicando que todos los campos deben estar llenos
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Muestra el cuadro de diálogo
        dialog.show()
    }


    @SuppressLint("MissingInflatedId")
    private fun mostrarDialogRecuperarDatos(
        email: String,
        nombre: String?,
        edad: String?,
        rol: String,
        sexo: String,
        telefono: String?
    ) {
        // Crear un AlertDialog.Builder
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.datosrecuperados)

        // Inflar el diseño personalizado para el contenido del diálogo
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_recuperar_datos, null)

        // Obtener las referencias a los TextView dentro del diseño personalizado
        val txtEmail = dialogLayout.findViewById<TextView>(R.id.txtEmail)
        val txtNombre = dialogLayout.findViewById<TextView>(R.id.txtNombre)
        val txtEdad = dialogLayout.findViewById<TextView>(R.id.txtEdad)
        val txtRol = dialogLayout.findViewById<TextView>(R.id.txtRol)
        val txtSexo = dialogLayout.findViewById<TextView>(R.id.txtSexo)
        val txtTelefono = dialogLayout.findViewById<TextView>(R.id.txtTelefono)

        // Establecer el texto en los TextView
        txtEmail.text = email
        txtNombre.text = nombre ?: "Nombre no disponible"
        txtEdad.text = edad ?: "Edad no disponible"
        txtRol.text = if (rol.isNotEmpty()) "Rol: $rol" else "Rol no disponible"
        txtSexo.text = "Género: $sexo"
        txtTelefono.text = telefono ?: "Edad no disponible"

        // Establecer el diseño personalizado en el diálogo
        builder.setView(dialogLayout)

        // Agregar un botón "Aceptar" al diálogo
        builder.setPositiveButton(R.string.aceptar) { dialog, _ ->
            dialog.dismiss() // Cierra el diálogo al hacer clic en "Aceptar"
        }

        // Mostrar el diálogo
        builder.show()
    }

    // Función para mostrar elementos relacionados con datos personales
    private fun mostrarDatosPersonales() {


        binding.btEliminar.visibility = View.VISIBLE
        binding.btRecuperar.visibility = View.VISIBLE

    }

    private fun ocultarDatosPersonales() {


        binding.btEliminar.visibility = View.GONE
        binding.btRecuperar.visibility = View.GONE


    }


    // Función para ir a la VentanaPrincipal
    private fun irAVentanaPrincipal(email: String) {
        ocultarDatosPersonales()
        val intent = Intent(this, VentanaPrincipal::class.java).apply {
            putExtra("email", email)

            putExtra("nombre", intent.getStringExtra("nombre"))
        }
        startActivity(intent)
        finish()  // Cierra la actividad actual para que no puedas volver atrás
    }


    private fun crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canalImportancia = NotificationManager.IMPORTANCE_HIGH
            val canal = NotificationChannel(canalId, canalNombre, canalImportancia)

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            manager.createNotificationChannel(canal)
        }
    }

    private fun crearNotificacion(email: String) {
        // Cambié el resultIntent para que apunte a la misma actividad o a ninguna (null)
        val resultIntent: Intent? = null
        val resultPendingIntent: PendingIntent? = resultIntent?.let {
            TaskStackBuilder.create(applicationContext).run {
                addNextIntentWithParentStack(it)
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }

        val notificacion = NotificationCompat.Builder(this, canalId).also {
            it.setContentTitle("$email")
            it.setContentText("HAS CERRADO SESIÓN")
            it.setSmallIcon(R.drawable.mesage)
            it.priority = NotificationCompat.PRIORITY_HIGH
            it.setContentIntent(resultPendingIntent)
            it.setAutoCancel(true)
        }.build()

        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(notificacionId, notificacion)
    }
// Método para enviar SMS
    private fun enviarSMS(numeroTelefono: String) {
        // Agrega aquí la lógica para enviar el SMS
        // Puedes utilizar la clase SmsManager para enviar el mensaje de texto

        val smsManager = SmsManager.getDefault()
        val mensaje = "Has eliminado tu Usuario :("

        smsManager.sendTextMessage(numeroTelefono, null, mensaje, null, null)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_SMS_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permiso concedido, puedes mostrar un mensaje
                    // Ahora que los permisos son concedidos, puedes enviar el SMS
                    enviarSMS(numeroTelefono)
                } else {
                    // Permiso denegado, informar al usuario
                    Toast.makeText(this, "Permiso denegado para enviar SMS", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // Función para abrir la Ventana UsoStorage
    private fun abrirVentanaUsoStorage2(email: String) {
        val intent = Intent(this, UsoStorage2::class.java)
        // Puedes agregar el id como un extra si es necesario
        intent.putExtra("email", email)
        startActivity(intent)
    }

}
