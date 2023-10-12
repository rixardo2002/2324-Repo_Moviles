package com.example.encuestargr


data class Encuesta(
    val nombre: String?,
    val sistemaOperativo: String?,
    val especialidad: String?,
    val horas: Int?
) {
    override fun toString(): String {
        return "$nombre," + " $sistemaOperativo," + " $especialidad," + " $horas"
    }

}