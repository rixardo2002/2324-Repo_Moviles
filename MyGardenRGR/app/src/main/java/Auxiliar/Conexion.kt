package Auxiliar

import Conexion.AdminSQLIteConexion

import android.content.ContentValues
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mygardenrgr.Producto

object Conexion {
    //Clase tipo Singleton para acceder e métodos sin tener que crear objeto (similar a Static de Java)
    //Si hay algún cambio en la BBDD, se cambia el número de versión y así automáticamente
    // se pasa por el OnUpgrade del AdminSQLite
    //y ahí añades las sentencias que interese modificar de la BBDD
    private  var DATABASE_NAME = "productos2.db3"
    private  var DATABASE_VERSION = 1


    fun cambiarBD(nombreBD:String){
        this.DATABASE_NAME = nombreBD
    }

    fun addProducto(contexto: AppCompatActivity, p: Producto):Long{
        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.writableDatabase
        val registro = ContentValues()
        registro.put("email", p.email)
        registro.put("nombre",p.nombre)
        registro.put("procedencia",p.procedencia)
        registro.put("cantidad",p.cantidad)
        registro.put("imagen",p.imagen)

        val codigo = bd.insert("productos", null, registro)
        bd.close()
        return codigo
    }

    fun delProducto(contexto: AppCompatActivity, nombre: String):Int{
        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.writableDatabase
        val cant = bd.delete("productos", "nombre=?", arrayOf(nombre))
        bd.close()
        return cant
    }

    fun modProducto(contexto:AppCompatActivity, nombre:String, p:Producto):Int {
        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.writableDatabase
        val registro = ContentValues()
        registro.put("procedencia", p.procedencia)
        registro.put("cantidad", p.cantidad)

        val cant = bd.update("productos", registro, "nombre=?", arrayOf(nombre))
        bd.close()
        return cant
    }

    fun buscarProducto(contexto: AppCompatActivity, nombre:String):Producto? {
        var p:Producto? = null //si no encuentra ninguno vendrá null, por eso la ? y también en la devolución de la función.
        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.readableDatabase
        /*Esta funciona pero es mejor como está hecho justo debajo con ?
        val fila = bd.rawQuery(
            "select nombre,edad from personas where dni='${dni}'",
            null
        )*/
        val fila =bd.rawQuery(
            "SELECT email,procedencia, cantidad,imagen FROM productos WHERE nombre=?",
            arrayOf(nombre)
        )
        //en fila viene un CURSOR, que está justo antes del primero por eso lo ponemos en el primero en la siguiente línea
        if (fila.moveToFirst()) {//si no hay datos el moveToFirst, devuelve false, si hay devuelve true.
            p = Producto( fila.getString(0),nombre, fila.getString(1), fila.getString(2), fila.getString(3))
        }
        bd.close()
        return p
    }

    fun obtenerProductos(contexto: AppCompatActivity, emailUsuario: String):ArrayList<Producto>{
        var productos:ArrayList<Producto> = ArrayList(1)

        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.readableDatabase
        var par = arrayOf(emailUsuario)
        Log.d("ABCD",emailUsuario)
        val fila = bd.rawQuery("select * from productos where email= ?",par)
        while (fila.moveToNext()) {
            var p:Producto = Producto(fila.getString(0),fila.getString(1),fila.getString(2),fila.getString(3),fila.getString(4))
            productos.add(p)
        }
        bd.close()
        Log.d("ABCD",productos.toString())
        return productos //este arrayList lo puedo poner en un adapter de un RecyclerView por ejemplo.
    }

}