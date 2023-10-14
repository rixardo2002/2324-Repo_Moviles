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

        mostrarSeekBar()
            binding.bttnVal.setOnClickListener(){
                var seleccionado = rellenarCampos()
                mostrarSeekBar()
                if (seleccionado){
                    listaEncuestas = crearEncuesta()
                }else{
                    Toast.makeText(this, "Tienes que rellenar todos los campos", Toast.LENGTH_SHORT).show()
                }
                limpiar()

            }

        binding.bttnR.setOnClickListener(){
            listaEncuestas.clear()
            limpiar()


        }
        binding.bttnC.setOnClickListener(){
            val cuantas = listaEncuestas.size
            val mensaje = "Número de encuestas creadas: $cuantas"
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        }

        binding.bttnR2.setOnClickListener(){

            val textoEncuestas = StringBuilder()

            for (encuesta in listaEncuestas) {
                textoEncuestas.append(encuesta.toString()).append("\n")
            }

            binding.txtMLine.setText(textoEncuestas)
        }

    }
    private fun rellenarCampos():Boolean{
        var seleccionado = false
        val sistemaOperativoSeleccionado = binding.radButW.isChecked || binding.radButM.isChecked || binding.radButL.isChecked
        val especialidadSeleccionada = binding.radButDAM.isChecked || binding.radButDAW.isChecked || binding.radButA.isChecked

        if (sistemaOperativoSeleccionado && especialidadSeleccionada) {
            seleccionado = true
        } else {
            Toast.makeText(this, "Tienes que rellenar todos los campos", Toast.LENGTH_SHORT).show()
        }
        return seleccionado
    }

    private fun eleccionSistOperativo(): String {

        var seleccion = "Desconocido"
        if (binding.radButM.isChecked) {
            seleccion = "Mac"
        } else if (binding.radButW.isChecked) {
            seleccion = "Windows"
        } else if (binding.radButL.isChecked) {
            seleccion = "Linux"
        }


        return seleccion
    }
    private fun eleccionEspecialidad(): String {

        var especialidad = "Desconocida"

        if (binding.radButDAM.isChecked) {
            especialidad = "DAM"
        } else if (binding.radButDAW.isChecked) {
            especialidad = "DAW"
        } else if (binding.radButA.isChecked) {
            especialidad = "ASIR"
        }

        return especialidad
    }
    private fun eleccionHoras(): Int {
        return binding.seekBar.progress
    }

    private fun mostrarSeekBar(){
        binding.seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                var progreso = binding.seekBar.progress

                binding.txtViexNum.setText(progreso.toString())
            }

            override fun onStartTrackingTouch(seek: SeekBar) {

            }

            override fun onStopTrackingTouch(seek: SeekBar) {

                var progreso = binding.seekBar.progress
                binding.txtViexNum.setText(progreso.toString())
            }

        })
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

        binding.editTxtN.text.clear()
        binding.switch1.isChecked = false
        binding.seekBar.progress = 0
        binding.radioGroupSistemaOperativo.clearCheck()
        binding.radioGroupEspecialidad.clearCheck()
        binding.txtMLine.setText("")

    }
}