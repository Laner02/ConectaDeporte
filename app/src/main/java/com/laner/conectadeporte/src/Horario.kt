package com.laner.conectadeporte.src

// Clase Kotlin que representa las franjas horarias que crean la lista horario de los cursos
class Horario(dia: Dia_Semana, hora_inicio: Float, hora_fin: Float) {

    private val dia : Dia_Semana
    private val hora_inicio : Float
    private val hora_fin : Float

    init {
        this.dia = dia
        this.hora_inicio = hora_inicio
        this.hora_fin = hora_fin
    }

    fun getDia() : Dia_Semana {
        return dia
    }

    fun getHoraInicio() : Float {
        return hora_inicio
    }

    fun getHoraFin() : Float {
        return hora_fin
    }
}