package com.example.mygardenrgr

import Adaptadores.MiAdaptadorRecycler
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.KeyListener
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygardenrgr.databinding.ActivityVentanaListaBinding
import com.example.mygardenrgr.databinding.DialogAddProductoBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class VentanaLista : AppCompatActivity() {

    private var edNombreKeyListener: KeyListener? = null
    private var edRazaKeyListener: KeyListener? = null
    private var edEdadKeyListener: KeyListener? = null
    private var edIdKeyListener: KeyListener? = null

    val TAG = "RGRPC"
    lateinit var binding: ActivityVentanaListaBinding
    lateinit var miAdaptadorRecycler: MiAdaptadorRecycler

    lateinit var firestore: FirebaseFirestore

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var contextoPrincipal: Context

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVentanaListaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        miAdaptadorRecycler = MiAdaptadorRecycler(this, this)


        // Obtener la lista de productos desde Almacen

        val email = intent.getStringExtra("email").toString()
        contextoPrincipal = this
        binding.edEmailL.text = email

        // Inicializa Firestore
        firestore = FirebaseFirestore.getInstance()

        // Obtiene el rol del usuario y muestra u oculta el botón según el rol
        obtenerYMostrarRol(email)


        // Configura el RecyclerView con el id listaProductosRecycler
        binding.listaProductosRecycler.layoutManager = LinearLayoutManager(this)


        // Configura el adaptador en el RecyclerView con el id listaProductosRecycler
        binding.listaProductosRecycler.adapter = miAdaptadorRecycler

        // Cargar datos directamente desde el Almacen
        cargarDatosDesdeFirestore()

        // Configura el botón de inserción
        binding.btnAdd.setOnClickListener {
            mostrarDialogInsertar()
        }

        binding.btVolverr.setOnClickListener {
            finish()
        }
    }

    private fun obtenerYMostrarRol(email: String) {
        // Búsqueda por id del documento.
        firestore.collection("users")
            .document(email)
            .get()
            .addOnSuccessListener { document ->
                // Si encuentra el documento será satisfactorio este listener y entraremos en él.
                val rol = document.get("rol") as String? ?: ""

                // Mostrar el rol en el TextView
                binding.edRol.text = "${rol.toLowerCase()}"

                // Configura el rol en el adaptador
                miAdaptadorRecycler.setRol(rol)

                // Lógica para mostrar u ocultar el botón según el rol
                Log.d(TAG, "${rol.toLowerCase()}")

                // Verifica el rol y muestra u oculta el botón según sea necesario
                if (rol == "TRABAJADOR") {
                    binding.btnAdd.visibility = View.VISIBLE
                    Log.d(TAG, "Se muestra el botón")
                }
                else {
                    binding.btnAdd.visibility = View.GONE
                    if (rol=="CLIENTE"){
                        miAdaptadorRecycler.setEmail(email)
                        Log.d(TAG, "Es cliente")
                        Log.d(TAG, "${email.toLowerCase()}")
                    }else{
                        mostrarDialogoRegistro()
                    }

                    Log.d(TAG, "Valor de 'rol': $rol")
                }

            }
            .addOnFailureListener {
                Log.e(TAG, "Error al obtener el rol del usuario", it)
            }
    }
    private fun mostrarDialogoRegistro() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro necesario")
            .setMessage("Deberías registrarte para acceder a esta función.")
            .setPositiveButton("Aceptar") { dialog, _ ->
                // Puedes agregar aquí la lógica para dirigir al usuario a la pantalla de registro si es necesario
                dialog.dismiss()
            }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun insertarDatosEnFirestore(nombre: String, raza: String, edad: String, id: String,descripcion:String) {
        // Obtiene los valores de los EditText
        val nombre = nombre
        val raza = raza
        val edad = edad
        val id = id
        val descripcion=descripcion
        // Verifica que los campos no estén vacíos antes de la inserción
        if (nombre.isNotEmpty() && raza.isNotEmpty() && edad.isNotEmpty()) {
            // Crea un nuevo objeto Producto con los valores ingresados
            val nuevoProducto = Producto(nombre, raza, edad, id,descripcion)

            // Agrega el nuevo producto a la colección "animales" en Firestore
            firestore.collection("animales")
                .document(id)
                .set(nuevoProducto)
                .addOnSuccessListener {
                    // Muestra un mensaje de éxito (puedes ajustar según tus necesidades)
                    showToast("Perro insertado con éxito")


                    // Carga nuevamente los datos desde Firestore y actualiza el RecyclerView
                    cargarDatosDesdeFirestore()
                }
                .addOnFailureListener { e ->
                    // Maneja la excepción si ocurre un error durante la inserción
                    showToast("Error al insertar el perro")
                }
        } else {
            showToast("Completa todos los campos antes de insertar")
        }
    }

    fun cargarDatosDesdeFirestore() {
//        val currentUserEmail = firebaseAuth.currentUser?.email
//        Log.d(TAG, "Email del usuario: $currentUserEmail")

        //if (currentUserEmail != null) {
        val animalesCollection = firestore.collection("animales")
        animalesCollection.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Obtención de animales exitosa")
                    val listaAnimales = mutableListOf<Producto>()

                    for (document in task.result!!) {
                        try {
                            // Obtener los datos del animal del documento
                            val nombre = document.getString("nombre")!!
                            val raza = document.getString("raza")!!
                            val edad = document.getString("edad")!!
                            val id = document.getString("id")!!
                            val descripcion = document.getString("descripcion")!!

                            // Crear un objeto Producto con los datos obtenidos
                            val animal = Producto(nombre, raza, edad, id,descripcion)
                            listaAnimales.add(animal)
                        } catch (e: Exception) {
                            Log.e(TAG, "Error al obtener datos del animal: ${e.message}")
                        }
                    }

                    // Configurar la lista de animales en el adaptador y notificar los cambios
                    // Asignar directamente la lista de animales al adaptador
                    miAdaptadorRecycler.actualizarProductos(listaAnimales)
                    miAdaptadorRecycler.notifyDataSetChanged()
                } else {
                    Log.e(TAG, "Error al obtener datos desde Firestore: ${task.exception}")
                    showToast("Error al cargar datos desde Firestore")
                }
            }
        //}
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun mostrarDialogInsertar() {
        val binding: DialogAddProductoBinding = DialogAddProductoBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this)
            .setView(binding.root)
            .setTitle("Añadir Perro")

        val alertDialog = builder.create()

        // Variable de bandera para verificar si se ha añadido una imagen
        var imagenAgregada = false

        // Manejar la lógica del botón Añadir Imagen
        binding.btnAddImage.setOnClickListener {
            // Obtén el ID ingresado por el usuario
            val id = binding.edIdL.text.toString()

            // Realiza la verificación de existencia del ID en la base de datos
            verificarExistenciaId(id) { idExiste ->
                if (idExiste) {
                    showToast("¡Error! El ID '$id' ya existe en la base de datos.")
                } else {
                    // Si el ID no existe, permite que el usuario añada una imagen
                    // Abre la nueva actividad (UsoStorage) y pasa los datos
                    abrirVentanaUsoStorage(id)

                    // Establece la variable de bandera en true
                    imagenAgregada = true

                    // Deshabilita la edición de los TextInputEditText después de añadir la imagen
                    deshabilitarEdicion(binding)
                }
            }
        }

        // Manejar la lógica del botón de guardar
        binding.btnGuardar.setOnClickListener {
            // Obtén los valores de los campos de entrada
            val nombre = binding.edNombreL.text.toString()
            val raza = binding.edRazaL.text.toString()
            val edad = binding.edEdadL.text.toString()
            val id = binding.edIdL.text.toString()
            val descripcion = binding.edDescripcion.text.toString()

            // Realiza la lógica de guardar en Firestore o donde sea necesario
            // ...

            // Verificar si se ha agregado una imagen
            if (!imagenAgregada) {
                // Mostrar un Toast indicando que es necesario añadir una imagen
                showToast("Hay que añadir una imagen")
                return@setOnClickListener
            }

            // Llama a insertarDatosEnFirestore() con los valores obtenidos
            insertarDatosEnFirestore(nombre, raza, edad, id,descripcion)


            // Cuando necesites volver a habilitar la edición (por ejemplo, cuando se presiona el botón para insertar un animal)
            binding.edNombreL.keyListener = edNombreKeyListener
            binding.edRazaL.keyListener = edRazaKeyListener
            binding.edEdadL.keyListener = edEdadKeyListener
            binding.edIdL.keyListener = edIdKeyListener

            // Después de la inserción, limpiamos los campos del diálogo
            limpiarCamposDialog(binding)
            // Cierra el diálogo
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
    private fun verificarExistenciaId(id: String, callback: (Boolean) -> Unit) {
        firestore.collection("animales")
            .document(id)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                callback(documentSnapshot.exists())
            }
            .addOnFailureListener {
                Log.e(TAG, "Error al verificar la existencia del ID en la base de datos", it)
                callback(false)
            }
    }
    private fun deshabilitarEdicion(binding: DialogAddProductoBinding) {
        // Guardar los key listeners originales
        edNombreKeyListener = binding.edNombreL.keyListener
        edRazaKeyListener = binding.edRazaL.keyListener
        edEdadKeyListener = binding.edEdadL.keyListener
        edIdKeyListener = binding.edIdL.keyListener

        // Deshabilitar la edición
        binding.edNombreL.keyListener = null
        binding.edRazaL.keyListener = null
        binding.edEdadL.keyListener = null
        binding.edIdL.keyListener = null
    }

    private fun habilitarEdicion(binding: DialogAddProductoBinding) {
        // Habilitar la edición utilizando los key listeners originales
        binding.edNombreL.keyListener = edNombreKeyListener
        binding.edRazaL.keyListener = edRazaKeyListener
        binding.edEdadL.keyListener = edEdadKeyListener
        binding.edIdL.keyListener = edIdKeyListener
    }


    // Función para abrir la Ventana UsoStorage
    private fun abrirVentanaUsoStorage(id: String) {
        val intent = Intent(this, UsoStorage::class.java)
        // Puedes agregar el id como un extra si es necesario
        intent.putExtra("id", id)
        startActivity(intent)
    }

    private fun limpiarCamposDialog(binding: DialogAddProductoBinding) {
        // Limpiar los campos de EditText y habilitar la edición
        binding.edNombreL.text?.clear()
        binding.edRazaL.text?.clear()
        binding.edEdadL.text?.clear()
        binding.edIdL.text?.clear()


    }

    // Método para mostrar el menú contextual
    // Método para mostrar el menú contextual
    fun showContextMenu(view: View, position: Int) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.context_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_item_eliminar -> {
                    // Manejar la acción de eliminar
                    showDeleteConfirmationDialog(position)
                    true
                }

                R.id.menu_item_actualizar -> {
                    // Llamar a la función para mostrar el diálogo de actualización
                    Log.e(TAG, "ACTUALIZAR")
                    Log.e(TAG, "${position}")
                    miAdaptadorRecycler.obtenerYMostrarProductoPorId(position)

                    true
                }

                else -> false
            }
        }

        popup.show()
    }

    fun showDeleteConfirmationDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar elemento")
            .setMessage("¿Estás seguro de que deseas eliminar este elemento?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarElemento(position)
                Log.e(TAG, "${position}")
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    fun eliminarElemento(position: Int) {
        val producto = miAdaptadorRecycler.productos[position]

        // Elimina el elemento del RecyclerView
        miAdaptadorRecycler.productos =
            miAdaptadorRecycler.productos.filterIndexed { index, _ -> index != position }
        miAdaptadorRecycler.notifyDataSetChanged()

        // Elimina el elemento de Firebase Firestore
        firestore.collection("animales")
            .document(producto.id)
            .delete()
            .addOnSuccessListener {
                showToast("Elemento eliminado con éxito")
            }
            .addOnFailureListener {
                showToast("Error al eliminar el elemento")
            }

        eliminarImagenStorage(producto.id)
    }

    // Método para eliminar una imagen del Firebase Storage
    fun eliminarImagenStorage(nombreImagen: String) {
        // Obtiene la referencia al Firebase Storage
        val storage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = storage.reference

        // Construye la referencia a la imagen que deseas eliminar
        val refFoto: StorageReference = storageRef.child("images/$nombreImagen")

        // Elimina la imagen
        refFoto.delete().addOnSuccessListener {
            // Éxito al eliminar la imagen
            // Puedes realizar acciones adicionales si es necesario
        }.addOnFailureListener {
            // Fallo al eliminar la imagen
            // Puedes manejar el error o mostrar un mensaje al usuario
        }
    }

}