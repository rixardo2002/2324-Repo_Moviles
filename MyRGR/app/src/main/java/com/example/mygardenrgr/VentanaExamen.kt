package com.example.mygardenrgr

import Auxiliar.Conexion
import Conexion.AdminSQLIteConexion
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mygardenrgr.databinding.ActivityVentanaExamenBinding

class VentanaExamen : AppCompatActivity() {
    private val canalNombre = "not-RGR"
    private val canalId = "canalId"
    private val notificacionId = 0

    private lateinit var binding: ActivityVentanaExamenBinding
    private lateinit var adminSQLite: AdminSQLIteConexion

    private lateinit var textViewPregunta: TextView
    private lateinit var buttonOpcion1: Button
    private lateinit var buttonOpcion2: Button
    private lateinit var buttonOpcion3: Button
    private lateinit var buttonIniciar: Button

    private var numPreguntaActual = 0
    private var numAciertos = 0

    private val preguntas = arrayOf(
        "¿Cuál es una de las razones más importantes para pasear a tu perro regularmente?",
        "¿Cuánto tiempo en promedio necesita un perro adulto para dormir cada día?",
        "¿Qué es esencial para la salud dental de un perro?",
        "¿Cuál es la etapa de vida en la que los cachorros son más susceptibles a socializar?",
        "¿Qué grupo de alimentos debe evitarse dar a los perros ya que puede ser tóxico para ellos?",
        "¿Cuál es una señal de que tu perro puede estar enfermo?",
        "¿Cuál es la mejor manera de identificar a tu perro en caso de pérdida?",
        "¿Qué vacuna es esencial para prevenir la rabia en perros?",
        "¿Cuál es una buena práctica al socializar a tu perro con otros perros?",
        "¿Cuál es un signo de estrés en un perro?",
        "¿Cuál es una forma efectiva de entrenar a tu perro?",
        "¿Cuál es la mejor manera de mantener la salud mental de tu perro?",
        "¿Cuándo es un buen momento para comenzar a entrenar a un cachorro?",
        "¿Qué hacer si tu perro come algo peligroso, como chocolate?",
        "¿Cuál es un beneficio de la esterilización/castración de tu perro?",
        "¿Qué debe proporcionarse regularmente para mantener las uñas de tu perro en buen estado?",
        "¿Cuál es un signo de buena salud en las heces de tu perro?",
        "¿Cuál es un comportamiento común en un perro ansioso?",
        "¿Cómo debes abordar a un perro desconocido?",
        "¿Cuál es una práctica importante al alimentar a tu perro?"
    )

    private val opciones = arrayOf(
        arrayListOf("Ejercicio físico", "Juegos en casa", "Dormir"),
        arrayListOf("4 horas", "8 horas", "12 horas"),
        arrayListOf("Cepillado regular", "Dejar que mastique piedras", "Ignorar su dentición"),
        arrayListOf("1-3 meses", "6-8 meses", "12-14 meses"),
        arrayListOf("Frutas", "Verduras", "Chocolate"),
        arrayListOf("Aumento del apetito", "Pérdida de apetito", "Ambos"),
        arrayListOf("Collar con etiqueta de identificación", "Peluquería frecuente", "Dejarlo vagar sin supervisión"),
        arrayListOf("Vacuna contra la tos de las perreras", "Vacuna contra la leptospirosis", "Vacuna contra la rabia"),
        arrayListOf("Evitar el contacto con otros perros", "Dejar que se aproximen sin restricciones", "Observar las señales de lenguaje corporal y tener encuentros controlados"),
        arrayListOf("Cola levantada", "Orejas relajadas", "Lamerse los labios frecuentemente"),
        arrayListOf("Castigos físicos", "Reforzamiento positivo", "Ignorar su comportamiento"),
        arrayListOf("Dejarlo solo por largos períodos", "Proporcionar juguetes y estimulación mental", "Limitar el ejercicio físico"),
        arrayListOf("Después de 2 años", "Tan pronto como llegue a casa", "Después de 6 meses"),
        arrayListOf("Esperar a ver si se siente mal", "Llamar al veterinario de inmediato", "Darle más chocolate para neutralizarlo"),
        arrayListOf("Aumenta la agresividad", "Reduce el riesgo de ciertos problemas de salud", "No tiene impacto en la salud"),
        arrayListOf("Agua caliente", "Juguetes masticables", "Recorte de uñas"),
        arrayListOf("Color verde", "Consistencia firme", "Presencia de moco"),
        arrayListOf("Cola en alto", "Jugar de manera enérgica", "Temblores y evitación"),
        arrayListOf("Acercarse rápidamente", "Extender la mano para que te huela", "Ignorarlo completamente"),
        arrayListOf("Alimentarlo una vez al día", "Cambiar su comida con frecuencia", "Establecer horarios regulares de alimentación")
          // Puedes reemplazar esto con las opciones específicas para la segunda pregunta, etc.
        // ... Continuar con las opciones para cada pregunta
    )

    private val respuestasCorrectas= arrayOf(0,1,0,0,2,1,0,2,2,2,1,1,1,1,1,2,1,2,1,2)

    private var IndicePregunta = 0
    private var puntuaje = 0
    private var emailActual: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentanaExamenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar el correo electrónico del Intent
        val email: String? = intent.getStringExtra("email")
        emailActual = email
        // Log del email recibido
        Log.e("RGRPC", "Email recibido: $email")

        // Verificar si hay un examen guardado y mostrar un Toast si es así
        if (email != null) {
            val examenGuardado = Conexion.obtenerExamen(this, email)
            if(examenGuardado!=null){
                mostrarDialogoDatosActuales(examenGuardado)
            }
            // Resto del código...
        } else {
            // Manejar el caso donde "email" es nulo, por ejemplo, mostrar un mensaje de error
            Toast.makeText(this, "No se proporcionó un correo electrónico", Toast.LENGTH_SHORT).show()
        }

        displayPreguntas()

        binding.buttonOpcion1.setOnClickListener{
            correcionRespuesta(0)
        }

        binding.buttonOpcion2.setOnClickListener{
            correcionRespuesta(1)
        }

        binding.buttonOpcion3.setOnClickListener{
            correcionRespuesta(2)
        }

        binding.buttonReiniciar.setOnClickListener{
            restartQuiz()
        }
        binding.buttonEliminar.setOnClickListener{
            eliminarDatosExamenActual()
        }

        // Almacenar el email al iniciar sesión
        val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.apply()

        // Obtener el email en cualquier parte de tu aplicación
        fun obtenerEmailUsuario(): String? {
            val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
            return sharedPreferences.getString("email", null)
        }

    }//override

    @SuppressLint("ResourceAsColor")
    private fun botonCorrectoColores(buttonIndex: Int){
        //md_theme_dark_errorContainer
        //md_theme_dark_inversePrimary
        // para de normal md_theme_light_onTertiaryContainer
        when(buttonIndex){
            0 -> binding.buttonOpcion1.setBackgroundColor(Color.GREEN)
            1 -> binding.buttonOpcion2.setBackgroundColor(Color.GREEN)
            2 -> binding.buttonOpcion3.setBackgroundColor(Color.GREEN)

        }
    }
    @SuppressLint("ResourceAsColor")
    private fun botonIncorrectoColores(buttonIndex: Int){
        //md_theme_dark_errorContainer
        //md_theme_dark_inversePrimary
        // para de normal md_theme_light_onTertiaryContainer
        when(buttonIndex){
            0 -> binding.buttonOpcion1.setBackgroundColor(Color.RED)
            1 -> binding.buttonOpcion2.setBackgroundColor(Color.RED)
            2 -> binding.buttonOpcion3.setBackgroundColor(Color.RED)

        }
    }
    @SuppressLint("ResourceAsColor")
    private fun reiciciarBotonesColores(){
        //md_theme_dark_errorContainer
        //md_theme_dark_inversePrimary
        // para de normal md_theme_light_onTertiaryContainer
        binding.buttonOpcion1.setBackgroundColor(Color.WHITE)
        binding.buttonOpcion2.setBackgroundColor(Color.WHITE)
        binding.buttonOpcion3.setBackgroundColor(Color.WHITE)

    }
    private fun muestraResultados() {
        Toast.makeText(this, "Tu puntacion es de $puntuaje sobre ${preguntas.size}", Toast.LENGTH_LONG).show()

        // Recuperar el correo electrónico del Intent
        val email: String? = intent.getStringExtra("email")

        if (email != null) {
            // Verificar si hay un examen guardado para este correo electrónico
            val examenGuardado = Conexion.obtenerExamen(this, email)

            if (examenGuardado != null) {
                // Si ya hay un examen guardado, mostrar un diálogo para actualizar los datos
                mostrarDialogoActualizarExamen(examenGuardado, email)
            } else {
                // Si no hay un examen guardado, crear uno nuevo y guardarlo
                val nuevoExamen = Examen(email = email, puntuacion = puntuaje, pregunta = IndicePregunta)
                Conexion.addExamen(this, nuevoExamen)
                binding.buttonReiniciar.isEnabled = true
                binding.buttonEliminar.isEnabled = true

            }
        } else {
            // Manejar el caso donde "email" es nulo, por ejemplo, mostrar un mensaje de error
            Toast.makeText(this, "No se proporcionó un correo electrónico", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarDialogoActualizarExamen(examenGuardado: Examen, email: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Actualizar Examen")
        builder.setMessage("Ya hay un examen guardado para este correo electrónico. ¿Quieres actualizarlo con los nuevos datos?")

        builder.setPositiveButton("Sí") { dialog, _ ->
            // Actualizar el examen guardado con los nuevos datos
            examenGuardado.puntuacion = puntuaje
            examenGuardado.pregunta = IndicePregunta

            // Guardar el examen actualizado en la base de datos
            Conexion.modExamen(this, email, examenGuardado)

            binding.buttonReiniciar.isEnabled = true
            binding.buttonEliminar.isEnabled = true
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            // No hacer nada si el usuario elige no actualizar el examen
            binding.buttonReiniciar.isEnabled = true
            binding.buttonEliminar.isEnabled = true

            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }


    private fun displayPreguntas(){
        binding.textViewPregunta.text=preguntas[IndicePregunta]
        binding.buttonOpcion1.text = opciones[IndicePregunta][0]
        binding.buttonOpcion2.text = opciones[IndicePregunta][1]
        binding.buttonOpcion3.text = opciones[IndicePregunta][2]
        reiciciarBotonesColores()
    }

    private fun correcionRespuesta(indiceRespSelec: Int) {
        val correctIndiceRespuesta = respuestasCorrectas[IndicePregunta]

        if (indiceRespSelec == correctIndiceRespuesta) {
            puntuaje++
            botonCorrectoColores(indiceRespSelec)
        } else {
            botonIncorrectoColores(indiceRespSelec)
            botonCorrectoColores(correctIndiceRespuesta)
        }

        if (IndicePregunta < preguntas.size - 1) {
            IndicePregunta++
            binding.textViewPregunta.postDelayed({ displayPreguntas() }, 1000)
        } else {
            muestraResultados()
        }
    }


    private fun restartQuiz(){
        IndicePregunta=0
        puntuaje=0
        displayPreguntas()
        binding.buttonReiniciar.isEnabled=false
    }

    private fun mostrarDialogoDatosActuales(examen: Examen) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Datos Actuales")
        builder.setMessage("Correo electrónico: ${examen.email}\nPuntuación: ${examen.puntuacion}\nPregunta: ${examen.pregunta}")

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
            // Resto del código después de hacer clic en Aceptar
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun eliminarDatosExamenActual() {
        val email: String? = intent.getStringExtra("email")
        if (email != null) {
            // Eliminar el examen asociado al correo electrónico actual
            Conexion.delExamen(this, email)
            Toast.makeText(this, "Datos del examen eliminados", Toast.LENGTH_SHORT).show()
        } else {
            // Manejar el caso donde "email" es nulo, por ejemplo, mostrar un mensaje de error
            Toast.makeText(this, "No se proporcionó un correo electrónico", Toast.LENGTH_SHORT).show()
        }
    }


}