

package Conexion

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class AdminSQLIteConexion(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        Log.e("ACSCO", "Paso por OnCreate del AdminSQLLite")
        /*
        Este método se lanza automáticamente cuando se ejecuta SQLite por primera vez (función sobrrescrita ya que es obligatoria)
        Aquí crearíamos las tablas y claves si son más de una.
        Se crean la primera vez si no existe. Pero tener en cuenta que aquí se guardarán configuraciones
        y pequeñas cosas, por lo tanto tampoco se meterán grandes sentencias ya que SQLite no está pensado para eso.
        Para bases de datos más complejas, usaremos servicios externos.
        */
        db.execSQL("CREATE TABLE examen(email TEXT PRIMARY KEY, puntuacion INTEGER, pregunta INTEGER)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.e("ACSCO", "Paso por OnUpgrade del AdminSQLLite")
        // Si la base de datos ya existe, se modificará con el SQL que aquí pongamos.
        // Si no existe, se va al OnCreate; si existe, viene aquí.
        // Para venir aquí, has tenido que pasar a una versión diferente, y se detecta automáticamente y pasa por aquí.
        // Por ejemplo, podríamos borrar una tabla con DROP y luego crearla si interesa que una tabla esté siempre vacía
        // o le hago un truncate y me cargo sus datos directamente (por ejemplo, la típica tabla de registro de bitácora de la sesión).
    }
}
