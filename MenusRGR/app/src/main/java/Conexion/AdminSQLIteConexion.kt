package Conexion

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class AdminSQLIteConexion(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        Log.e("ACSCO","Paso por OnCreate del AdminSQLLite")
        /*
        Este método se lanza automáticamente cuando se ejecuta SQLite por primera vez (función sobrrescrita ya que es obigatoria)
        Aquí crearíamos las tablas y claves si son más de una.
        Se crean la primera vez si no existe. Pero tener en cuenta que aquí se gaurdarán configuraciones
        y pequeñas cosas, por lo tanto tampoco se metarán grandes sentencias yq que SQLite no está pensado para eso
        Para BBDD más complejas, ya usarmeos servicios externos.
        */
        db.execSQL("create table usuarios(nombre text primary key, puntuacion int)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.e("ACSCO", "Paso por OnUpgrade del AdminSQLLite")

        // Si detectamos que la versión anterior es 1 y la nueva versión es 2, aplicamos cambios en la estructura de la base de datos.

        }
}

