package Adaptadores

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mygardenrgr.Producto
import com.example.mygardenrgr.R
import com.example.mygardenrgr.UsoStorage
import com.example.mygardenrgr.VentanaDetalle
import com.example.mygardenrgr.VentanaLista
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException


class MiAdaptadorRecycler(private val context: Context,private val activityReference: VentanaLista) :
    RecyclerView.Adapter<MiAdaptadorRecycler.ViewHolder>() {
    val TAG = "RGRPC"
    var productos: List<Producto> = mutableListOf()

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var rol: String = ""
    private var emailCli: String=""
    companion object {
        var seleccionado: Int = -1
    }

//    // Método para actualizar la lista de productos
fun actualizarProductos(productos: List<Producto>) {
    this.productos = productos
    notifyDataSetChanged()
}
    // Método para establecer el rol
    fun setRol(rol: String) {
        this.rol = rol
    }
    fun setEmail(emailCli: String) {
        this.emailCli = emailCli
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista =
            LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)



        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = productos[position]
        holder.bind(item, position, this)

        // Configura el setOnLongClickListener aquí
        holder.itemView.setOnLongClickListener {
            if (rol == "TRABAJADOR") {
                // Muestra el menú contextual al mantener presionado
                activityReference.showContextMenu(holder.itemView, position)
            }
            true
        }
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreProducto = view.findViewById(R.id.txtNombre) as TextView
        val raza = view.findViewById(R.id.txtProcedencia) as TextView
        val edad = view.findViewById(R.id.txtCantidad) as TextView
        val imagen = view.findViewById(R.id.imgImagen) as ImageView

//        init {
//
//
//            itemView.setOnLongClickListener {
//
//                if (rol == "TRABAJADOR") {
//                    Log.e(TAG,"${rol}")
//                    // Muestra el cuadro de diálogo para confirmar la eliminación
//                    // Muestra el menú contextual al mantener presionado
//                    Log.e(TAG,"${adapterPosition}")
//                    showDeleteConfirmationDialog(adapterPosition)
//
//                }
//                true
//            }
//
//        }

        fun bind(producto: Producto, pos: Int, miAdaptadorRecycler: MiAdaptadorRecycler) {
            nombreProducto.text = producto.nombre
            raza.text = producto.raza
            edad.text = producto.edad


            // Aquí obtienes la referencia a Firebase Storage
            val storage = FirebaseStorage.getInstance()
            val storageRef: StorageReference = storage.reference

            // Aquí construyes la referencia a la imagen en Storage
            val imgPerfil = producto.id // Asegúrate de tener esta propiedad en TuTipoDeDatos
            Log.e(TAG,"${producto.id}")

            val refFoto: StorageReference = storageRef.child("images/$imgPerfil")

            // Aquí descargas la imagen y la cargas en el ImageView
            try {
                val localFile = File.createTempFile("tempImage", "jpg")
                refFoto.getFile(localFile).addOnSuccessListener {
                    val bitmap: Bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    imagen.setImageBitmap(bitmap)

                }.addOnFailureListener {
                    Log.e("TuAdaptador", "Fallo al cargar la foto del usuario")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (pos == seleccionado) {

                // Acción al hacer clic en un elemento seleccionado
                val intent = Intent(context, VentanaDetalle::class.java)
                intent.putExtra("obj", producto)
                intent.putExtra("rol", rol) // Reemplaza 'rol' con la variable que contiene el rol del cliente
                intent.putExtra("emailCli", emailCli) // Reemplaza 'emailCli' con la variable que contiene el email del cliente
                context.startActivity(intent)
                seleccionado = -1


            } else {
                // Elemento no seleccionado
                with(nombreProducto) {
                    this.setTextColor(context.resources.getColor(R.color.md_theme_light_onPrimaryContainer))
                }
                with(edad) {
                    this.setTextColor(context.resources.getColor(R.color.md_theme_light_onPrimaryContainer))
                }
                with(raza) {
                    this.setTextColor(context.resources.getColor(R.color.md_theme_light_onPrimaryContainer))
                }
            }

            // Listener para clic en un elemento
            itemView.setOnClickListener {
                if (pos == seleccionado) {
                    seleccionado = -1
                } else {
                    seleccionado = pos
                }
                notifyDataSetChanged()
            }

        }
    }
    fun obtenerYMostrarProductoPorId(position: Int) {
        val producto = productos[position]
        // Búsqueda por id del documento en la colección "animales".
        firestore.collection("animales")
            .document(producto.id)
            .get()
            .addOnSuccessListener { document ->
                // Si encuentra el documento será satisfactorio este listener y entraremos en él.
                if (document.exists()) {
                    // Obtener el producto del documento
                    val producto = document.data
                    Log.e(TAG, "${producto}")
                    // Aquí puedes hacer lo que necesites con el producto obtenido.
                    // Por ejemplo, mostrar detalles en una interfaz de usuario.
                    showUpdateDialog(producto)

                } else {
                    // El documento no existe, puedes manejar este caso si es necesario.
                    Log.d(TAG, "No se encontró ningún producto con el ID: $producto.id")
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Error al obtener el producto de la colección 'animales'", it)
            }
    }

    private fun showUpdateDialog(productoData: Map<String, Any>?) {
        val context = context
        var dialog: AlertDialog? = null  // Declarar la variable dialog aquí

        if (productoData != null) {
            val builder = AlertDialog.Builder(context)
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.dialog_add_producto, null)
            builder.setView(view)

            val edNombre = view.findViewById<EditText>(R.id.edNombreL)
            val edId = view.findViewById<EditText>(R.id.edIdL)
            val edRaza = view.findViewById<EditText>(R.id.edRazaL)
            val edEdad = view.findViewById<EditText>(R.id.edEdadL)
            val edDescripcion = view.findViewById<EditText>(R.id.edDescripcion)

            // Establecer los valores iniciales en el diálogo
            edNombre.setText(productoData["nombre"].toString())
            edId.setText(productoData["id"].toString())
            edRaza.setText(productoData["raza"].toString())
            edEdad.setText(productoData["edad"].toString())
            edDescripcion.setText(productoData["descripcion"].toString())
            // Variable de bandera para verificar si se ha añadido una imagen
            // Deshabilitar la edición del EditText edId
            edId.keyListener = null
            var imagenAgregada = false

            // Manejar la lógica del botón Añadir Imagen
            view.findViewById<Button>(R.id.btnAddImage).setOnClickListener {
                // Coloca aquí la lógica para añadir imagen según tus necesidades
                // Puedes abrir una galería, cámara, etc.
                // Por ahora, simplemente establecemos la variable de bandera en true
                // Ahora, abre la nueva actividad (UsoStorage) y pasa los datos
                val id = edId.text.toString()
                abrirVentanaUsoStorage(id)

                imagenAgregada = true

                // Deshabilitar la edición de los TextInputEditText después de añadir la imagen
                edNombre.keyListener = null
                edRaza.keyListener = null
                edEdad.keyListener = null
                edId.keyListener = null
                edDescripcion.keyListener= null
            }
            // Configurar el botón de guardar en el diálogo
            view.findViewById<Button>(R.id.btnGuardar).setOnClickListener {
                // Obtener los nuevos datos del diálogo
                val nuevosDatos = hashMapOf(
                    "nombre" to edNombre.text.toString(),
                    "id" to productoData["id"].toString(),
                    "raza" to edRaza.text.toString(),
                    "edad" to edEdad.text.toString(),
                    "descripcion" to edDescripcion.text.toString()
                )

                // Actualizar el elemento en Firestore
                actualizarDocumentoEnFirestore(productoData["id"].toString(), nuevosDatos)

                // Cerrar el diálogo después de actualizar
                dialog?.dismiss()  // Usar el operador de seguridad ?. para evitar NPE
            }

            // Mostrar el diálogo
            dialog = builder.create()
            dialog.show()
        } else {
            showToast("Error al obtener datos del producto")
        }
    }
    // Función para abrir la Ventana UsoStorage
    private fun abrirVentanaUsoStorage(id: String) {
        val intent = Intent(context, UsoStorage::class.java)
        // Puedes agregar el id como un extra si es necesario
        intent.putExtra("id", id)
        context.startActivity(intent)
    }


    private fun actualizarDocumentoEnFirestore(id: String, datosActualizar: Map<String, Any>) {
        // Referencia al documento en la colección "animales"
        val documento = firestore.collection("animales").document(id)

        // Actualizar el documento con los nuevos datos
        documento
            .update(datosActualizar)
            .addOnSuccessListener {
                Log.e(TAG,"Producto actualizado correctamente.")
                activityReference.cargarDatosDesdeFirestore()
            }
            .addOnFailureListener {
                Log.e(TAG,"Error al actualizar el producto.")
            }
    }

    private fun showToast(s: String) {

    }

} //Clas MiadaptadorRecycler