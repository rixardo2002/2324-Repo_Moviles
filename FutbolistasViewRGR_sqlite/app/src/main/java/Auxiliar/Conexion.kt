package Auxiliar

import Conexion.AdminSQLIteConexion
import Modelo.Futbolista

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity

object Conexion {
    //Clase tipo Singleton para acceder e métodos sin tener que crear objeto (similar a Static de Java)
    //Si hay algún cambio en la BBDD, se cambia el número de versión y así automáticamente
    // se pasa por el OnUpgrade del AdminSQLite
    //y ahí añades las sentencias que interese modificar de la BBDD
    private  var DATABASE_NAME = "administracion.db3"
    private  var DATABASE_VERSION = 1


    fun cambiarBD(nombreBD:String){
        this.DATABASE_NAME = nombreBD
    }

    fun addPersona(contexto: AppCompatActivity, p: Futbolista):Long{
        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.writableDatabase //habilito la BBDD para escribir en ella, tambié deja leer.
        val registro = ContentValues() //objeto de kotlin, contenido de valores, un Map. Si haceis ctrl+clic lo veis.
        registro.put("id", p.id)
        registro.put("nombre",p.nombre)
        registro.put("apellido", p.apellido)
        registro.put("equipo",p.equipo)
        val codigo = bd.insert("personas", null, registro)
        bd.close()
        return codigo
    }

    fun delPersona(contexto: AppCompatActivity, id: String):Int{
        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.writableDatabase
        //val cant = bd.delete("personas", "dni='${dni}'", null)
        val cant = bd.delete("futbolistas", "id=?", arrayOf(id.toString()))
        bd.close()
        return cant
    }

    fun modPersona(contexto:AppCompatActivity, id:String, p:Futbolista):Int {
        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.writableDatabase
        val registro = ContentValues()

        registro.put("nombre", p.nombre)
        registro.put("apellido", p.apellido)
        registro.put("equipo", p.equipo)
       // val cant = bd.update("personas", registro, "dni='${dni}'", null)
        val cant = bd.update("futbolistas", registro, "id=?", arrayOf(id.toString()))
        //val cant = bd.update("personas", registro, "dni=? AND activo=?", arrayOf(dni.toString(), activo.toString()))
        //Esta línea de más arriba es para tener un ejemplo si el where tuviese más condiciones
        //es mejor la forma de la línea 49 que la de la línea 48, ya que es peligroso inyectar sql directamente al controlarse peor los errores
        //cant trae los datos actualizados.
        bd.close()
        return cant
    }

    fun buscarPersona(contexto: AppCompatActivity, id:String):Futbolista? {
        var p:Futbolista? = null //si no encuentra ninguno vendrá null, por eso la ? y también en la devolución de la función.
        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.readableDatabase
        /*Esta funciona pero es mejor como está hecho justo debajo con ?
        val fila = bd.rawQuery(
            "select nombre,edad from personas where dni='${dni}'",
            null
        )*/
        val fila =bd.rawQuery(
            "SELECT nombre,apellido,equipo FROM futbolistas WHERE id=?",
            arrayOf(id.toString())
        )
        //en fila viene un CURSOR, que está justo antes del primero por eso lo ponemos en el primero en la siguiente línea
        if (fila.moveToFirst()) {//si no hay datos el moveToFirst, devuelve false, si hay devuelve true.
            p = Futbolista(id, fila.getString(0), fila.getString(1),fila.getString(2))
        }
        bd.close()
        return p
    }

    fun obtenerPersonas(contexto: AppCompatActivity):ArrayList<Futbolista>{
        var futbolistas:ArrayList<Futbolista> = ArrayList(1)

        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.readableDatabase
        val fila = bd.rawQuery("select id,nombre,apellidos,equipo from futbolistas", null)
        while (fila.moveToNext()) {
            var p:Futbolista = Futbolista(fila.getString(0),fila.getString(1),fila.getString(2),fila.getString(3))
            futbolistas.add(p)
        }
        bd.close()
        return futbolistas //este arrayList lo puedo poner en un adapter de un RecyclerView por ejemplo.
    }

}