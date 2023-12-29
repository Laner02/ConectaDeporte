package com.laner.conectadeporte.src

import android.location.Location
import android.media.Image

// Clase Kotlin para almacenar los datos de un curso de la aplicacion
class Course(title: String, description: String, professor: String, location: String, ubicacion: Ubicacion, price: Float) {

    // private var c_id: Int (TODO debería tener un id la clase? o esto es mejor en la BD?)
    private var title: String
    private var description: String
    private var image: Image?               // La interrogacion indica que puede ser nulo, inicializar luego
    private var professor: String
    private var location: String            // Localizacion dentro de la provincia
    private var ubicacion: Ubicacion        // Ubicacion para poder filtarlos
    private var price: Float
    private var horarios: ArrayList<Horario>?

    init {
        this.title = title
        this.description = description
        image = null
        this.professor = professor
        this.location = location
        this.ubicacion = ubicacion
        this.price = price
        horarios = ArrayList()
    }

    // TODO hacer getters y setters, o poner los atts public pero eso da problemas
    fun getTitle() : String {
        return title
    }

    fun getDescription() : String {
        return description
    }

    fun getImage() : Image? {
        return image
    }

    fun getProfessor() : String {
        return professor
    }

    fun getLocation() : String {
        return location
    }

    fun getUbicacion(): Ubicacion {
        return ubicacion
    }

    fun getPrice() : Float {
        return price
    }

    fun getHorarios() : ArrayList<Horario>? {
        return horarios
    }

    fun addHorario(nuevo_horario: Horario) {
        horarios!!.add(nuevo_horario)
    }
}