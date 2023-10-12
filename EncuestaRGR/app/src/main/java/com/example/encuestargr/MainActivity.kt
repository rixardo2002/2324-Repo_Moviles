package com.example.encuestargr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.security.identity.PersonalizationData
import android.widget.SeekBar
import android.widget.Toast
import com.example.encuestargr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var listaEncuestas = ArrayList<Encuesta>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bttnVal.setOnClickListener(){
            listaEncuestas = crearEncuesta()
            limpiar()
            binding.txtMLine.setText("")

        }

        binding.bttnR.setOnClickListener(){
            listaEncuestas.clear()
            limpiar()
            binding.txtMLine.setText("")

        }
        binding.bttnC.setOnClickListener(){
            var cuantas = listaEncuestas.size
            Toast.makeText(this, cuantas, Toast.LENGTH_SHORT).show()
        }

        binding.bttnR2.setOnClickListener(){

            for (encuesta in listaEncuestas) {
                binding.txtMLine.setText(encuesta.toString())
            }
        }

    }


    private fun eleccionSistOperativo(): String {
        var seleccion = "Desconocido"
        var seleccionado = false


        while (!seleccionado) {

            if (binding.radButM.isChecked || binding.radButW.isChecked || binding.radButL.isChecked) {
                seleccionado = true  // Marca la variable como verdadera para salir del bucle

                if (binding.radButM.isChecked) {
                    seleccion = "Mac"
                } else if (binding.radButW.isChecked) {
                    seleccion = "Windows"
                } else if (binding.radButL.isChecked) {
                    seleccion = "Linux"
                }
            }else{
                Toast.makeText(this, "Debes seleccionar al menos una opción", Toast.LENGTH_SHORT).show()
            }
        }
        return seleccion
    }
    private fun eleccionEspecialidad(): String {

        var seleccionado = false
        var especialidad = "Desconocida"
        while (!seleccionado) {

            if (binding.radButDAM.isChecked || binding.radButDAW.isChecked || binding.radButA.isChecked) {
                seleccionado = true  // Marca la variable como verdadera para salir del bucle
                if (binding.radButDAM.isChecked) {
                    especialidad = "DAM"
                } else if (binding.radButDAW.isChecked) {
                    especialidad = "DAW"
                } else if (binding.radButA.isChecked) {
                    especialidad = "ASIR"
                }

            }else{
                Toast.makeText(this, "Debes seleccionar al menos una opción", Toast.LENGTH_SHORT).show()
            }
        }
        return especialidad
    }
    private fun eleccionHoras(): Int {
        var progreso = 0

        binding.seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                progreso = progress
                binding.txtViexNum.text = progreso.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                TODO("Not yet implemented")
            }

        })

        return progreso
    }
private fun crearEncuesta(): ArrayList<Encuesta>{
    var nombre: String

    if (binding.switch1.isChecked){
        nombre="Anonimo"
    }else{
       nombre = binding.editTxtN.text.toString()
    }


    var sistemaOperativo = eleccionSistOperativo()
    var especialidad = eleccionEspecialidad()
    var horas = eleccionHoras()
    var nuevaEncuesta = Encuesta(nombre, sistemaOperativo, especialidad, horas)
    listaEncuestas.add(nuevaEncuesta)

    return listaEncuestas
}
    private fun limpiar() {
        // Limpia el contenido del EditText (Plain Text)
        binding.editTxtN.text.clear()

        // Deselecciona los RadioButtons
        binding.radButM.isChecked = false
        binding.radButW.isChecked = false
        binding.radButL.isChecked = false
        binding.radButDAM.isChecked = false
        binding.radButDAW.isChecked = false
        binding.radButA.isChecked = false
        binding.switch1.isChecked = false
        binding.seekBar.progress = 0
    }

}