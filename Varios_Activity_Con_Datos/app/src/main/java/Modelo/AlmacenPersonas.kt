package Modelo

object AlmacenPersonas {

    var personas = ArrayList<Persona>()

    fun aniadirPersona(p:Persona){
        personas.add(p)
    }
}