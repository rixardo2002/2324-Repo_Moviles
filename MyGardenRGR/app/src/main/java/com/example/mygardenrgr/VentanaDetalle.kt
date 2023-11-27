package com.example.mygardenrgr

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mygardenrgr.databinding.ActivityMainBinding
import com.example.mygardenrgr.databinding.ActivityVentanaDetalleBinding

class VentanaDetalle : AppCompatActivity() {

    lateinit var binding: ActivityVentanaDetalleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityVentanaDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var p  = intent.getSerializableExtra("obj") as Producto
        var nombre=p.nombre
        var procedencia=p.procedencia
        var imagen=p.imagen
        var cantidad=p.cantidad
        var contenido=contenidoDescripcion(nombre)

        binding.textName.text=nombre
        binding.textPlace.text=procedencia
        binding.textCant.text=cantidad
        eleccionImagen(imagen)
        binding.btnVolver.setOnClickListener {
            finish()
        }
        // Agregar OnClickListener al botón
        binding.verMasButton.setOnClickListener {
            val contenido = contenidoDescripcion(nombre)
            mostrarDialogoDescripcion(contenido)
        }
    }

    // Función para mostrar información en un cuadro de diálogo o Toast
    private fun mostrarInformacionCompleta(descripcion: String) {
        // Puedes usar un cuadro de diálogo o un Toast para mostrar la descripción completa
        // Ejemplo de Toast:
        Toast.makeText(this, descripcion, Toast.LENGTH_LONG).show()

        // Ejemplo de cuadro de diálogo (descomenta la línea siguiente si prefieres un cuadro de diálogo)
        // mostrarDialogo(descripcion)
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

    private fun contenidoDescripcion(nombre:String):String{
        var descripcion: String = ""

        if (nombre.equals("Sandia")) {
            descripcion = "La sandía es una fruta refrescante y jugosa, compuesta principalmente de agua. Tiene una pulpa de color rojo brillante con semillas negras y una cáscara verde. Es popular en climas cálidos y es consumida comúnmente en verano."
        } else if (nombre.equals("Melon")) {
            descripcion = "El melón es una fruta dulce y jugosa con pulpa de color naranja o verde, dependiendo de la variedad. Tiene una cáscara exterior rugosa y puede ser redondo o alargado. Es apreciado por su sabor refrescante."
        } else if (nombre.equals("Cebolla")) {
            descripcion = "La cebolla es una hortaliza con un bulbo redondo, de sabor picante cuando se come cruda y dulce cuando se cocina. Viene en varias variedades, como la cebolla blanca, amarilla y roja. Se utiliza comúnmente como ingrediente en muchas recetas."
        } else if (nombre.equals("Patata")) {
            descripcion = "La patata es un tubérculo que se consume en todo el mundo. Tiene una piel delgada y puede ser de color marrón, amarillo o morado. Se puede cocinar de diversas formas, como hervida, asada o frita."
        } else if (nombre.equals("Judia")) {
            descripcion = "Las judías verdes son vainas comestibles alargadas y verdes. Tienen un sabor crujiente y se cocinan de diversas maneras, ya sea hervidas, salteadas o al vapor. Son una fuente rica en fibra y nutrientes."
        } else if (nombre.equals("Berenjena")) {
            descripcion = "La berenjena es una hortaliza de forma alargada y piel morada o negra. Tiene una pulpa suave y se utiliza en diversas cocinas para preparar platos como la moussaka o la parmigiana."
        } else if (nombre.equals("Pimiento")) {
            descripcion = "Los pimientos pueden ser de diferentes colores, como rojo, amarillo, verde o naranja. Tienen un sabor dulce o ligeramente picante, dependiendo de la variedad. Se pueden consumir crudos en ensaladas o cocidos en diversas recetas."
        } else if (nombre.equals("Tomate")) {
            descripcion = "El tomate es una fruta roja y jugosa que se consume comúnmente como una hortaliza. Puede tener diversas formas y tamaños. Se utiliza en ensaladas, salsas, jugos y una variedad de platos culinarios."
        }

        return descripcion
    }

    private fun eleccionImagen(imagen: String) {
        val resourceId = resources.getIdentifier(imagen.toLowerCase(), "drawable", packageName)
        val imagenId = if (resourceId != 0) resourceId else R.drawable.verduras
        binding.imageView.setImageResource(imagenId)
    }
}