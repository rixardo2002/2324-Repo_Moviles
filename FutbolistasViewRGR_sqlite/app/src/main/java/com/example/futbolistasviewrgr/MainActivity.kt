package com.example.futbolistasviewrgr

import Auxiliar.Conexion
import Modelo.Futbolista
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.futbolistasviewrgr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var edDNI: EditText
    lateinit var edNombre: EditText
    lateinit var edApellido: EditText
    lateinit var edEquipo: EditText
    lateinit var botonAdd: Button
    lateinit var botonBuscar: Button
    lateinit var botonBorrar: Button
    lateinit var botonEditar: Button
    lateinit var txtListdo: TextView
    //En este proyecto el acceso a los controles y la creación de los eventos está hecha de la primera manera que vimos
    //El motivo es didáctico, para que no lo perdamos de vista porque a veces es útil o más rápido y viene bien que
    //estéis familiarizados, porque en una empresa os podéis encontrar esta manera de hacerlo.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("ACSCO","Paso por OnCreate ")
        setContentView(R.layout.activity_main)
        edDNI = findViewById(R.id.edDNI)
        edDNI.requestFocus()//poner el foco en el primer txt para q siempre empiece ahi
        edNombre = findViewById<EditText>(R.id.edNombre)
        edApellido = findViewById<EditText>(R.id.edApellido)
        edEquipo = findViewById<EditText>(R.id.edEquipo)
        botonAdd = findViewById(R.id.btnAdd)
        botonBuscar = findViewById(R.id.btnBuscar)
        botonBorrar = findViewById(R.id.btnBorrar)
        botonEditar = findViewById(R.id.btnEditar)
        txtListdo = findViewById(R.id.txtListado)
    }

    fun addFutbolista(view: View) {
        if (edDNI.text.toString().trim().isEmpty() || edNombre.text.toString().trim().isEmpty()
            || edApellido.text.toString().trim().isEmpty() || edEquipo.text.toString().trim().isEmpty() ){
            Toast.makeText(this, "Campos en blanco", Toast.LENGTH_SHORT).show()
        }
        else {
            var futs: Futbolista = Futbolista(
                edDNI.getText().toString(),
                edNombre.getText().toString(),
                edApellido.getText().toString(),
                edEquipo.getText().toString()
            )
            var codigo= Conexion.addFutbolista(this, futs)
            edDNI.setText("")
            edNombre.setText("")
            edApellido.setText("")
            edEquipo.setText("")
            edDNI.requestFocus()
            //la L es por ser un Long lo que trae codigo.
            if(codigo!=-1L) {
                Toast.makeText(this, "Futbolista insertado", Toast.LENGTH_SHORT).show()
                listarFutbolistas(view)
            }
            else
                Toast.makeText(this, "Ya existe ese DNI. Futbolista NO insertado", Toast.LENGTH_SHORT).show()
        }
    }

    fun delFutbolistas(view: View) {
        var cant = Conexion.delFutbolista(this, edDNI.text.toString())
        edDNI.setText("")
        edNombre.setText("")
        edApellido.setText("")
        edEquipo.setText("")
        if (cant == 1) {
            Toast.makeText(this, "Se borró la futbolista con ese DNI", Toast.LENGTH_SHORT).show()
            listarFutbolistas(view)
        }
        else
            Toast.makeText(this, "No existe una futbolista con ese DNI", Toast.LENGTH_SHORT).show()

    }

    fun modFutbolista(view: View) {
        if (edDNI.text.toString().trim().isEmpty()|| edNombre.text.toString().trim().isEmpty()
            || edApellido.text.toString().trim().isEmpty() || edEquipo.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Campos en blanco", Toast.LENGTH_SHORT).show()
        }
        else {
            var futs: Futbolista = Futbolista(
                edDNI.getText().toString(),
                edNombre.getText().toString(),
                edApellido.getText().toString(),
                edEquipo.getText().toString()
            )
            var cant = Conexion.modFutbolista(this, edDNI.text.toString(), futs)
            if (cant == 1)
                Toast.makeText(this, "Se modificaron los datos", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "No existe una futbolista con ese DNI", Toast.LENGTH_SHORT).show()
        }
        listarFutbolistas(view)
    }

    fun buscarFutbolista(view: View) {
        var f:Futbolista? = null
        f = Conexion.buscarFutbolista(this, edDNI.text.toString())
        if (f!=null) {
            edNombre.setText(f.nombre)
            edApellido.setText(f.apellido)
            edEquipo.setText(f.equipo)


        } else {
            Toast.makeText(this, "No existe una futbolista con ese DNI", Toast.LENGTH_SHORT).show()
        }

    }

    fun listarFutbolistas(view: View) {
        var listado:ArrayList<Futbolista> = Conexion.obtenerFutbolistas(this)

        txtListdo.setText("")

        if (listado.size==0) {
            Toast.makeText(this, "No existen datos en la tabla", Toast.LENGTH_SHORT).show()
        }
        else {
            for(p in listado){
                var cadena = p.id + ", " + p.nombre + ", " + p.apellido + ", " + p.equipo +"\r\n"
                txtListdo.append(cadena)
            }
        }
    }
}