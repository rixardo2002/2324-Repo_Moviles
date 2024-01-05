package com.example.mygardenrgr

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mygardenrgr.databinding.ActivityVentanaPrincipalBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class VentanaPrincipal : AppCompatActivity() {

    private val canalNombre = "not-RGR"
    private val canalId = "canalId"
    private val notificacionId = 0
    lateinit var binding:ActivityVentanaPrincipalBinding
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentanaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Obtener los valores del Intent
        val email = intent.getStringExtra("email").toString()


        binding.toolbarPrincipal.title = "DOG HOUSE"
        binding.toolbarPrincipal.subtitle = email ?: ""

//        crearCanalNotificacion()
//        crearNotificacion()

        //aquí simplemente inflo la toolBaar, pero aún no hay opciones ni botón home.
        setSupportActionBar(binding.toolbarPrincipal)


        //en las siguientes líneas hago que aparezca el botón de atrás (home) y genero su evento
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarPrincipal.setNavigationOnClickListener {
            Toast.makeText(this,"Pulsado el retroceso", Toast.LENGTH_SHORT).show()
        }

        // Asigna un clic a la ImageView
        binding.imgExamen.setOnClickListener {
            // Obtiene el correo electrónico de alguna manera, puedes pasarlo como parámetro aquí


            // Crea un Intent para abrir la VentanaExamen
            val intent = Intent(this, VentanaExamen::class.java)

            // Agrega el correo electrónico como extra al Intent
            intent.putExtra("email", email)

            // Inicia la nueva actividad con el Intent
            startActivity(intent)
        }

        binding.imgBttnHuerto.setOnClickListener(){
            // Crear un Intent para cambiar a la actividad VentanaRecycler
            val intent = Intent(this, VentanaLista::class.java)

            // Agregar el correo electrónico como un extra al Intent
            intent.putExtra("email", email)

            // Iniciar la nueva actividad
            startActivity(intent)

        }



    }//OVERRIDE



    //************************* Funciones auxiliares para los menú de puntos *****************************
    //la primera infla el menú que previamente hemos creado como resource, la segunda carga las opciones.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnPerson -> {
                // Aquí lanzas la actividad Home y le pasas los datos
                val intent = Intent(this, Home::class.java).apply {
                    putExtra("email", intent.getStringExtra("email"))
                    putExtra("provider", intent.getStringExtra("provider"))
                    putExtra("nombre", intent.getStringExtra("nombre"))
                }
                startActivity(intent)
                finish()
            }
            R.id.lenguageEnglish ->{
                changeLanguage("en")
            }
            R.id.lenguageSpanish ->{
                changeLanguage("es")
            }

            R.id.mnOp1 -> {
                abrirEnlace1()
            }



        }
        return super.onOptionsItemSelected(item)
    }
    private fun abrirEnlace1() {
        val url = "https://github.com/rixardo2002/2324-Repo_Moviles"
        abrirEnlace(url)
    }


    private fun abrirEnlace(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
    private fun changeLanguage(language: String) {
        val locale = Locale(language)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

        // Reinicia la actividad para que los cambios surtan efecto
        recreate()
    }

//    private fun crearCanalNotificacion(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val canalImportancia = NotificationManager.IMPORTANCE_HIGH
//            val canal = NotificationChannel(canalId, canalNombre, canalImportancia)
//
//            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//            manager.createNotificationChannel(canal)
//        }
//    }
//
//    private fun crearNotificacion(){
//
//        val resultIntent = Intent(applicationContext, Home::class.java)
//        val resultPendingIntent = TaskStackBuilder.create(applicationContext).run{
//            addNextIntentWithParentStack(resultIntent)
//            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
//        }
//        val notificacion = NotificationCompat.Builder(this,canalId).also {
//            it.setContentTitle("Bienvenido!!!")
//            it.setContentText("Registrate o Modifica tu usuario")
//            it.setSmallIcon(R.drawable.mesage)
//            it.priority = NotificationCompat.PRIORITY_HIGH
//            it.setContentIntent(resultPendingIntent)
//            it.setAutoCancel(true)
//        }.build()
//
//        val notificationManager = NotificationManagerCompat.from(this)
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//
//            return
//        }
//        notificationManager.notify(notificacionId,notificacion)
//
//    }


}