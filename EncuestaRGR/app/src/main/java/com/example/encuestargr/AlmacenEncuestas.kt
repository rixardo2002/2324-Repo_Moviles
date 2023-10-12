package com.example.encuestargr

object AlmacenEncuestas {

    var encuestas = ArrayList<Encuesta>()

    fun aniadirPersona(e: Encuesta){
        encuestas.add(e)
    }
}