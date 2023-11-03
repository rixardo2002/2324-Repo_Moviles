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
        db.execSQL("create table personas(dni text primary key, nombre text, apellido text, equipo text)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.e("ACSCO", "Paso por OnUpgrade del AdminSQLLite")
        Log.e("ACSCO", "Old Version: $oldVersion, New Version: $newVersion")

        // Verifica si la versión anterior es menor que la versión en la que se agregaron los campos "apellido" y "equipo".
        if (oldVersion < 2 && newVersion >= 2) {
            // Modifica la estructura de la tabla "personas" para agregar los campos "apellido" y "equipo".
            db.execSQL("ALTER TABLE personas ADD COLUMN apellido TEXT")
            db.execSQL("ALTER TABLE personas ADD COLUMN equipo TEXT")
        }

        // Puedes agregar más bloques if para otras actualizaciones según la versión.

        // Ejemplo de otro bloque if para futuras actualizaciones:
        /*
        if (oldVersion < 3 && newVersion >= 3) {
            // Realizar modificaciones adicionales aquí.
        }
        */
    }

}