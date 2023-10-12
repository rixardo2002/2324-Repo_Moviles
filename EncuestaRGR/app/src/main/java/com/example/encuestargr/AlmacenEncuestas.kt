package com.example.encuestargr

object AlmacenEncuestas {

    var encuestas = ArrayList<Encuesta>()

    fun aniadirEncuesta(e: Encuesta){
        encuestas.add(e)
    }
}