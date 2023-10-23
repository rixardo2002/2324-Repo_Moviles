package Modelo

object FactoriaListaFutbolista {
    fun generaLista(cant:Int):ArrayList<Futbolista> {
        var lista = ArrayList<Futbolista>(1)
        for(i in 1..cant){
            lista.add(FactoriaFutbolista.generaFutbolista())
        }
        return lista
    }
}