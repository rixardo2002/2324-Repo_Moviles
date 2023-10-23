package Modelo


object FactoriaFutbolista {
    fun generaFutbolista() : Futbolista {
        var nombres = listOf<String>("Cristiano", "Fernando", "Andres", "Leo", "Iker")
        var apellidos = listOf<String>("Ronaldo", "Torres", "Iniesta", "Casillas")
        var imagenes = listOf<String>("sevilla","madrid","betis");
        var nombreFutbolistas = nombres[(nombres.indices).random()]
        var f = Futbolista(nombreFutbolistas, apellidos[(apellidos.indices).random()],imagenes[(imagenes.indices).random()])
        return f
    }
}