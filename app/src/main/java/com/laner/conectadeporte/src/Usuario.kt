package com.laner.conectadeporte.src

// Clase Kotlin para usuarios de la aplicacion
class Usuario (correo: String, nombre: String, apellidos: String, telefono: String) {

    // El nombre de usuario deberia ser inmutable para no poder cambiar el identificador
    private var correo: String
    private var nombre: String
    private var apellidos: String
    private var telefono: String
    private var listaLocalidades: ArrayList<Ubicacion>?

    init {
        this.correo = correo
        this.nombre = nombre
        this.apellidos = apellidos
        this.telefono = telefono
        this.listaLocalidades = ArrayList()
    }

    fun getCorreo() : String {
        return correo
    }

    fun getNombre() : String {
        return nombre
    }

    fun getApellidos() : String {
        return apellidos
    }

    fun getNombreCompleto() :String {
        return nombre + apellidos
    }

    // No metemos un get de la contasenna? seria raro

    fun getTelefono() : String {
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