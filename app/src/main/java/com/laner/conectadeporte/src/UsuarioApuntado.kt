package com.laner.conectadeporte.src

import java.util.Date

// Clase Kotlin para los usuarios apuntados a un curso
// TODO metemos aqui un atributo de referencia al curso apuntado? pero luego habria que ir buscandolo
// o metemos en el curso una lista de usuarios apuntados, y pillamos que usuario de la app es por el atributo session
// TODO creamos clase DNI? con long y un char, un metodo tostring, y comprobacion de que un dni lo es cuando te lo pasan, en un metodo STATIC?
// TODO metemos una clase con las franjas de edad??
// Todos los campos son obligatorios
class UsuarioApuntado (nombre_apuntado: String, apellidos_apuntado: String, dni_apuntado: String,
                       fnac_apuntado: Date, horario_apuntado: Horario, edad_apuntado: Rango_Edad) {

    // Atributos
    private val nombre_apuntado: String
    private val apellidos_apuntado: String
    private val dni_apuntado: String
    private val fnac_apuntado: Date
    private val horario_apuntado: Horario
    private val edad_apuntado: Rango_Edad

    init {
        this.nombre_apuntado = nombre_apuntado
        this.apellidos_apuntado = apellidos_apuntado
        this.dni_apuntado = dni_apuntado
        this.fnac_apuntado = fnac_apuntado
        this.horario_apuntado = horario_apuntado
        this.edad_apuntado = edad_apuntado
    }

    fun getNombreApuntado() : String {
        return nombre_apuntado
    }

    fun getApellidosApuntado() : String {
        return apellidos_apuntado
    }

    fun getDniApuntado() : String {
        return dni_apuntado
    }

    fun getFnacApuntado() : Date {
        return fnac_apuntado
    }

    fun getHorarioApuntado() : Horario {
        return horario_apuntado
    }

    fun getEdadApuntado() : Rango_Edad {
        return edad_apuntado
    }
}