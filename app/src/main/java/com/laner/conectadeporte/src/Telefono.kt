package com.laner.conectadeporte.src

// Clase Kotlin especifica para los numeros de telefono de los usuarios
class Telefono(prefijo: Int, numero: Long) {

    private val prefijo : Int
    private val numero : Long

    init {
        this.prefijo = prefijo
        this.numero = numero
    }

    fun getPrefijo() : Int {
        return prefijo
    }

    fun getNumero() : Long {
        return numero
    }
}