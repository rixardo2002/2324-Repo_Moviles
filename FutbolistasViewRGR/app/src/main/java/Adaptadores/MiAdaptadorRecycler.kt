package Adaptadores

import Modelo.Almacen
import Modelo.Almacen.futbolistas
import Modelo.Futbolista

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.example.futbolistasviewrgr.R


class MiAdaptadorRecycler (var personajes : ArrayList<Futbolista>, var  context: Context) : RecyclerView.Adapter<MiAdaptadorRecycler.ViewHolder>() {

    companion object {
        //Esta variable estática nos será muy útil para saber cual está marcado o no.
        var seleccionado: Int = -1
        /*
        PAra marcar o desmarcar un elemento de la lista lo haremos diferente a una listView. En la listView el listener
        está en la activity por lo que podemos controlar desde fuera el valor de seleccionado y pasarlo al adapter, asociamos
        el adapter a la listview y resuelto.
        En las RecyclerView usamos para pintar cada elemento la función bind (ver código más abajo, en la clase ViewHolder).
        Esto se carga una vez, solo una vez, de ahí la eficiencia de las RecyclerView. Si queremos que el click que hagamos
        se vea reflejado debemos recargar la lista, para ello forzamos la recarga con el método: notifyDataSetChanged().
         */
    }


    /**
     * onBindViewHolder() se encarga de coger cada una de las posiciones de la lista de personajes y pasarlas a la clase
     * ViewHolder(clase interna, ver abajo) para que esta pinte todos los valores y active el evento onClick en cada uno.
     * position irá cambiando en cada iteración. Esta invocación a estos métodos lo hace automáticamente,sólo hay que sobreescribirlos
     * y personalizar con nuestro array list.
     * Esta a su vez llama a holder.bind, que está implementado más abajo.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = futbolistas.get(position)
        holder.bind(item, context, position, this)
    }

    /**
     *  Como su nombre indica lo que hará será devolvernos un objeto ViewHolder al cual le pasamos la celda que hemos creado.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        //return ViewHolder(layoutInflater.inflate(R.layout.item_lo,parent,false))
//        return ViewHolder(layoutInflater.inflate(R.layout.item_card,parent,false))

        //Este método infla cada una de las CardView

        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        val viewHolder = ViewHolder(vista)
        // Configurar el OnClickListener para pasar a la segunda ventana.


        return viewHolder
    }

    /**
     * getItemCount() nos devuelve el tamaño de la lista, que lo necesita el RecyclerView.
     */
    override fun getItemCount(): Int {
        //del array list que se pasa, el size, así sabe los elementos a pintar.
        return futbolistas.size
    }


    //--------------------------------- Clase interna ViewHolder -----------------------------------
    /**
     * La clase ViewHolder. No es necesaria hacerla dentro del adapter, pero como van tan ligadas
     * se puede declarar aquí.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //Esto solo se asocia la primera vez que se llama a la clase, en el método onCreate de la clase que contiene a esta.
        //Por eso no hace falta que hagamos lo que hacíamos en el método getView de los adaptadores para las listsViews.
        //val nombrePersonaje = view.findViewById(R.id.txtNombre) as TextView
        //val tipoPersonaje = view.findViewById(R.id.txtTipo) as TextView
        //val avatar = view.findViewById(R.id.imgImagen) as ImageView

        //Como en el ejemplo general de las listas (ProbandoListas) vemos que se puede inflar cada elemento con una card o con un layout.
        val nombreFutbolistas = view.findViewById(R.id.txtNombre) as TextView
        val apellidos = view.findViewById(R.id.txtApellido) as TextView
        val equipo = view.findViewById(R.id.imgImagen) as ImageView

        val btnDetalleEspcifico = view.findViewById<Button>(R.id.btDetalleCard) as Button

        /**
         * Éste método se llama desde el método onBindViewHolder de la clase contenedora. Como no vuelve a crear un objeto
         * sino que usa el ya creado en onCreateViewHolder, las asociaciones findViewById no vuelven a hacerse y es más eficiente.
         */
        @SuppressLint("ResourceAsColor")
        fun bind(
            futs: Futbolista,
            context: Context,
            pos: Int,
            miAdaptadorRecycler: MiAdaptadorRecycler
        ) {
            nombreFutbolistas.text = futs.nombre
            apellidos.text = futs.apellido
            val uri = "@drawable/" + futs.equipo
            val imageResource: Int =
                context.getResources().getIdentifier(uri, null, context.getPackageName())
            var res: Drawable = context.resources.getDrawable(imageResource)
            equipo.setImageDrawable(res)


            //Para marcar o desmarcar al seleccionado usamos el siguiente código.
            //comparo la posición y pinto en el color elegido(blue)
            //está implementado de dos maneras, uan deprecated y actual.


//            itemView.setOnLongClickListener(View.OnLongClickListener() {
//                Log.e("ACSC0","long click")
//            }

            //Se levanta una escucha para cada item. Si pulsamos el seleccionado pondremos la selección a -1, (deselecciona)
            // en otro caso será el nuevo sleccionado.
            itemView.setOnClickListener {
                if (pos == MiAdaptadorRecycler.seleccionado) {
                    MiAdaptadorRecycler.seleccionado = -1
                } else {
                    MiAdaptadorRecycler.seleccionado = pos
                    Log.e(
                        "ACSC0",
                        "Seleccionado: ${
                            Almacen.futbolistas.get(MiAdaptadorRecycler.seleccionado).toString()
                        }"
                    )
                }
                //Con la siguiente instrucción forzamos a recargar el viewHolder porque han cambiado los datos. Así pintará al seleccionado.

                miAdaptadorRecycler.notifyDataSetChanged()

//                val intent = Intent(context, MainActivity2::class.java)
//
//                context.startActivity(intent)

                Toast.makeText(
                    context,
                    "Valor seleccionado " + MiAdaptadorRecycler.seleccionado.toString(),
                    Toast.LENGTH_SHORT
                ).show()

            }
            itemView.setOnLongClickListener(View.OnLongClickListener {
                Log.e(
                    "ACSCO",
                    "Seleccionado con long click: ${Almacen.futbolistas.get(pos).toString()}"
                )
                Almacen.futbolistas.removeAt(pos)
                miAdaptadorRecycler.notifyDataSetChanged()
                true //Tenemos que devolver un valor boolean.
            })


        }
    }
}
