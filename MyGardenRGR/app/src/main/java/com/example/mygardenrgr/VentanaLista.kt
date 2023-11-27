package com.example.mygardenrgr

import Adaptadores.MiAdaptadorRecycler
import Auxiliar.Conexion
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mygardenrgr.databinding.ActivityMainBinding
import com.example.mygardenrgr.databinding.ActivityVentanaListaBinding

class VentanaLista : AppCompatActivity() {

    lateinit var binding: ActivityVentanaListaBinding
    lateinit var miRecyclerView: RecyclerView

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var contextoPrincipal: Context
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityVentanaListaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val email = intent.getStringExtra("email").toString()
        var listado:ArrayList<Producto> = Conexion.obtenerProductos(this)

        binding.edEmailL.text = email


        miRecyclerView = binding.listaProductosRecycler as RecyclerView
        miRecyclerView.setHasFixedSize(true)
        miRecyclerView.layoutManager = LinearLayoutManager(this)
        // Crear un nuevo adaptador con la lista actualizada de productos
        var miAdapter = MiAdaptadorRecycler(listado, this)
        miRecyclerView.adapter = miAdapter

        contextoPrincipal = this

        binding.btVolverr.setOnClickListener {
            finish()
        }

    }

    fun addProducto(view: View) {
        if (binding.edNombreL.text.toString().trim().isEmpty() || binding.edProcedenciaL.text.toString().trim().isEmpty()
            || binding.edCantidadL.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Campos en blanco", Toast.LENGTH_SHORT).show()
        }
        else {

            var prod: Producto = Producto(
                binding.edEmailL.toString(),
                binding.edNombreL.getText().toString(),
                binding.edProcedenciaL.getText().toString(),
                binding.edCantidadL.getText().toString(),
                binding.edNombreL.getText().toString().toLowerCase()
            )
            var codigo= Conexion.addProducto(this, prod)
            binding.edEmailL.setText("")
            binding.edNombreL.setText("")
            binding.edProcedenciaL.setText("")
            binding.edCantidadL.setText("")
            binding.edNombreL.requestFocus()
            //la L es por ser un Long lo que trae codigo.
            if(codigo!=-1L) {
                Toast.makeText(this, "Producto insertada", Toast.LENGTH_SHORT).show()
                listarProductos(view)
            }
            else
                Toast.makeText(this, "Ya existe ese nombre. Producto no insertada", Toast.LENGTH_SHORT).show()
        }
    }

    fun delProducto(view: View) {
        var cant = Conexion.delProducto(this, binding.edNombreL.text.toString())
        binding.edNombreL.setText("")
        binding.edCantidadL.setText("")
        binding.edProcedenciaL.setText("")
        if (cant == 1) {
            Toast.makeText(this, "Se borró el producto con ese nombre", Toast.LENGTH_SHORT).show()
            listarProductos(view)
        }
        else
            Toast.makeText(this, "No existe un producto con ese nombre", Toast.LENGTH_SHORT).show()

    }

    fun modProducto(view: View) {
        if (binding.edNombreL.text.toString().trim().isEmpty() || binding.edCantidadL.text.toString().trim().isEmpty()
            || binding.edProcedenciaL.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Campos en blanco", Toast.LENGTH_SHORT).show()
        }
        else {
            var prod: Producto = Producto(
                binding.edEmailL.toString(),
                binding.edNombreL.getText().toString(),
                binding.edProcedenciaL.getText().toString(),
                binding.edCantidadL.getText().toString(),
                binding.edNombreL.getText().toString()
            )
            var cant = Conexion.modProducto(this, binding.edNombreL.text.toString(), prod)
            if (cant == 1)
                Toast.makeText(this, "Se modificaron los datos", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "No existe un producto con ese nombre", Toast.LENGTH_SHORT).show()
        }
        listarProductos(view)
    }

    fun buscarProducto(view: View) {
        var p:Producto? = null
        p = Conexion.buscarProducto(this, binding.edNombreL.text.toString())
        if (p!=null) {
            binding.edProcedenciaL.setText(p.procedencia)
            binding.edCantidadL.setText(p.cantidad)
        } else {
            Toast.makeText(this, "No existe un producto con ese nombre", Toast.LENGTH_SHORT).show()
        }

    }

    fun listarProductos(view: View) {
        var listado:ArrayList<Producto> = Conexion.obtenerProductos(this)

        if (listado.size == 0){
            Toast.makeText(this, "No existen más datos", Toast.LENGTH_SHORT).show()
        }

        miRecyclerView = binding.listaProductosRecycler as RecyclerView
        miRecyclerView.setHasFixedSize(true)
        miRecyclerView.layoutManager = LinearLayoutManager(this)
        var miAdapter = MiAdaptadorRecycler(listado, this)
        miRecyclerView.adapter = miAdapter

        contextoPrincipal = this
    }











}