package Adaptadores

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.mygardenrgr.Producto
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mygardenrgr.Almacen
import com.example.mygardenrgr.R
import com.example.mygardenrgr.VentanaDetalle
import com.example.mygardenrgr.VentanaLista

class MiAdaptadorRecycler(var productos : ArrayList<Producto>, var  context: Context) : RecyclerView.Adapter<MiAdaptadorRecycler.ViewHolder>() {

    companion object {
        var seleccionado: Int = -1
    }

    /**
     * onBindViewHolder() se encarga de coger cada una de las posiciones de la lista de personajes y pasarlas a la clase
     * ViewHolder(clase interna, ver abajo) para que esta pinte todos los valores y active el evento onClick en cada uno.
     * position irá cambiando en cada iteración. Esta invocación a estos métodos lo hace automáticamente,sólo hay que sobreescribirlos
     * y personalizar con nuestro array list.
     * Esta a su vez llama a holder.bind, que está implementado más abajo.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = productos.get(position)
        holder.bind(item, context, position, this)
    }

    /**
     *  Como su nombre indica lo que hará será devolvernos un objeto ViewHolder al cual le pasamos la celda que hemos creado.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        val viewHolder = ViewHolder(vista)
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(context, VentanaDetalle::class.java)
            context.startActivity(intent)
        }

        return viewHolder
    }

    /**
     * getItemCount() nos devuelve el tamaño de la lista, que lo necesita el RecyclerView.
     */
    override fun getItemCount(): Int {
        return productos.size
    }


    //--------------------------------- Clase interna ViewHolder -----------------------------------
    /**
     * La clase ViewHolder. No es necesaria hacerla dentro del adapter, pero como van tan ligadas
     * se puede declarar aquí.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //Esto solo se asocia la primera vez que se llama a la clase, en el método onCreate de la clase que contiene a esta.
        //Por eso no hace falta que hagamos lo que hacíamos en el método getView de los adaptadores para las listsViews.
        //Como en el ejemplo general de las listas (ProbandoListas) vemos que se puede inflar cada elemento con una card o con un layout.
        val nombreProducto = view.findViewById(R.id.txtNombre) as TextView
        val ProcedenciaProducto = view.findViewById(R.id.txtProcedencia) as TextView
        val CantidadProducto = view.findViewById(R.id.txtCantidad) as TextView
        val imagen = view.findViewById(R.id.imgImagen) as ImageView

        val btnDetalleEspcifico = view.findViewById<Button>(R.id.btDetalleCard) as Button

        /**
         * Éste método se llama desde el método onBindViewHolder de la clase contenedora. Como no vuelve a crear un objeto
         * sino que usa el ya creado en onCreateViewHolder, las asociaciones findViewById no vuelven a hacerse y es más eficiente.
         */
        @SuppressLint("ResourceAsColor")
        fun bind(
            producto: Producto,
            context: Context,
            pos: Int,
            miAdaptadorRecycler: MiAdaptadorRecycler
        ) {
            nombreProducto.text = producto.nombre.toLowerCase()
            ProcedenciaProducto.text = producto.procedencia
            CantidadProducto.text = producto.cantidad

            var imagenes = listOf<String>("berenjena","cebolla","judia","melon","patata","pimiento","sandia","tomate");
            if (producto.imagen in imagenes){
                val uri = "@drawable/" + producto.imagen
                val imageResource: Int = context.getResources().getIdentifier(uri, null, context.getPackageName())
                var res: Drawable = context.resources.getDrawable(imageResource)
                imagen.setImageDrawable(res)
            }else{
                val uri = "@drawable/verduras"
                val imageResource: Int = context.getResources().getIdentifier(uri, null, context.getPackageName())
                var res: Drawable = context.resources.getDrawable(imageResource)
                imagen.setImageDrawable(res)
            }

            //Para marcar o desmarcar al seleccionado usamos el siguiente código.
            //comparo la posición y pinto en el color elegido(blue)
            //está implementado de dos maneras, uan deprecated y actual.

            if (pos == MiAdaptadorRecycler.seleccionado) {
                with(nombreProducto) {
                    this.setTextColor(resources.getColor(R.color.seed))

                }
                with(CantidadProducto) {
                    this.setTextColor(resources.getColor(R.color.seed))
                }
                with(ProcedenciaProducto) {
                    this.setTextColor(resources.getColor(R.color.seed))
                }
                var inte: Intent = Intent(VentanaLista.contextoPrincipal, VentanaDetalle::class.java)
                inte.putExtra("obj", producto)
                ContextCompat.startActivity(VentanaLista.contextoPrincipal, inte, null)

            } else {

                with(nombreProducto) {
                    this.setTextColor(resources.getColor(R.color.md_theme_light_onPrimaryContainer))
                }
                with(CantidadProducto) {
                    this.setTextColor(resources.getColor(R.color.md_theme_light_onPrimaryContainer))
                }
                with(ProcedenciaProducto) {
                    this.setTextColor(resources.getColor(R.color.md_theme_light_onPrimaryContainer))
                }
            }
            //Se levanta una escucha para cada item. Si pulsamos el seleccionado pondremos la selección a -1, (deselecciona)
            // en otro caso será el nuevo sleccionado.
            itemView.setOnClickListener {
                if (pos == MiAdaptadorRecycler.seleccionado) {
                    MiAdaptadorRecycler.seleccionado = -1
                } else {
                    MiAdaptadorRecycler.seleccionado = pos

                }
                //Con la siguiente instrucción forzamos a recargar el viewHolder porque han cambiado los datos. Así pintará al seleccionado.

                miAdaptadorRecycler.notifyDataSetChanged()

            }

            btnDetalleEspcifico.setOnClickListener {
                Log.e("Usuario", "Has pulsado el botón de ${producto}")
                var inte: Intent = Intent(VentanaLista.contextoPrincipal, VentanaDetalle::class.java)
                inte.putExtra("obj", producto)
                ContextCompat.startActivity(VentanaLista.contextoPrincipal, inte, null)
            }
        }

    }
}//Clas MiadaptadorRecycler