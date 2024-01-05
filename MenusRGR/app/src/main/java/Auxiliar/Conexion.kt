package Auxiliar

import Conexion.AdminSQLIteConexion
import Modelo.Usuario
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity

object Conexion {
    //Clase tipo Singleton para acceder e métodos sin tener que crear objeto (similar a Static de Java)
    //Si hay algún cambio en la BBDD, se cambia el número de versión y así automáticamente
    // se pasa por el OnUpgrade del AdminSQLite
    //y ahí añades las sentencias que interese modificar de la BBDD
    private var DATABASE_NAME = "usuarios.db3"
    private var DATABASE_VERSION = 1


    fun cambiarBD(nombreBD: String) {
        this.DATABASE_NAME = nombreBD
    }

    fun addUsuario(contexto: AppCompatActivity, p: Usuario): Long {
        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.writableDatabase //habilito la BBDD para escribir en ella, tambié deja leer.
        val registro =
            ContentValues() //objeto de kotlin, contenido de valores, un Map. Si haceis ctrl+clic lo veis.

        registro.put("nombre", p.nombre)
        registro.put("puntuacion", p.puntuacion.toString())
        val codigo = bd.insert("usuarios", null, registro)
        bd.close()
        return codigo
    }


    fun modPersona(contexto: AppCompatActivity, nombre: String, p: Usuario): Int {
        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.writableDatabase
        val registro = ContentValues()

        registro.put("puntuacion", p.puntuacion)
        // val cant = bd.update("personas", registro, "dni='${dni}'", null)
        val cant = bd.update("usuarios", registro, "nombre=?", arrayOf(nombre.toString()))
        //val cant = bd.update("personas", registro, "dni=? AND activo=?", arrayOf(dni.toString(), activo.toString()))
        //Esta línea de más arriba es para tener un ejemplo si el where tuviese más condiciones
        //es mejor la forma de la línea 49 que la de la línea 48, ya que es peligroso inyectar sql directamente al controlarse peor los errores
        //cant trae los datos actualizados.
        bd.close()
        return cant
    }


    fun buscarUsuario(contexto: AppCompatActivity, nombre: String): Usuario? {
        var p: Usuario? =
            null //si no encuentra ninguno vendrá null, por eso la ? y también en la devolución de la función.
        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.readableDatabase
        /*Esta funciona pero es mejor como está hecho justo debajo con ?
    val fila = bd.rawQuery(
        "select nombre,edad from personas where dni='${dni}'",
        null
    )*/
        val fila = bd.rawQuery(
            "SELECT puntuacion FROM usuarios WHERE nombre=?",
            arrayOf(nombre.toString())
        )
        //en fila viene un CURSOR, que está justo antes del primero por eso lo ponemos en el primero en la siguiente línea
        if (fila.moveToFirst()) {//si no hay datos el moveToFirst, devuelve false, si hay devuelve true.
            p = Usuario(nombre, fila.getInt(0))
        }
        bd.close()
        return p
    }


    fun obtenerUsuario(contexto: AppCompatActivity, nombre: String): Usuario? {

        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.readableDatabase
        val fila =
            bd.rawQuery("SELECT nombre, puntuacion FROM usuarios WHERE nombre=?", arrayOf(nombre))

        var usuario: Usuario? = null

        if (fila.moveToFirst()) {
            usuario = Usuario(fila.getString(0), fila.getInt(1))
        }

        bd.close()
        return usuario
    }

}