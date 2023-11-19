package com.laner.conectadeporte.src

// Clase Kotlin para usuarios de la aplicacion
class Usuario (id_usuario: String, nombre: String, apellidos: String, contrasena: String,
               correo: String?, telefono: Telefono?, listaLocalidades: ArrayList<Ubicacion>?) {

    // El nombre de usuario deberia ser inmutable para no poder cambiar el identificador
    private val id_usuario: String
    private var nombre: String
    private var apellidos: String
    private var contrasena: String
    private var correo: String?
    private var telefono: Telefono?
    private var listaLocalidades: ArrayList<Ubicacion>?

    init {
        this.id_usuario = id_usuario
        this.nombre = nombre
        this.apellidos = apellidos
        this.contrasena = contrasena
        this.correo = correo
        this.telefono = telefono
        this.listaLocalidades = ArrayList()
    }

    fun getIdUsuario() : String {
        return id_usuario
    }

    fun getNombre() : String {
        return nombre
    }

    fun getApellidos() : String {
        return apellidos
    }

    // No metemos un get de la contasenna? seria raro

    fun getCorreo() : String? {
        return correo
    }

    fun getTelefono() : Telefono? {
        return telefono
    }

    fun getListaLocalidades() : ArrayList<Ubicacion>? {
        return listaLocalidades
    }

    /**
     * Metodo para meter localidades en el historial
     */
    fun addUbicacionAHistorial(nueva_ubicacion: Ubicacion) {
        // Solo se puede añadir al historial una ubicacion con una llamada safe (?.), o una llamada cercionandose de que no es NULL (!!.)
        listaLocalidades!!.add(nueva_ubicacion)
    }
}