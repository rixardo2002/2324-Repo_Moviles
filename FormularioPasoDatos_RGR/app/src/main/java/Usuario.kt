data class Usuario(

val nombre:String,
    val apellido:String,
    val DNI:String,
    val gmail:String,
    val contrasenia:String


){
    override fun toString(): String {
        return "$nombre , $apellido , $DNI , $gmail , $contrasenia"
    }
}
