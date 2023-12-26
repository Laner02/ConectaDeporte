package com.laner.conectadeporte.ui.curso

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.laner.conectadeporte.R
import com.laner.conectadeporte.databinding.CursoFrameBinding
import com.google.firebase.database.*

class CursoFragment : Fragment() {

    // Variable del propio bindeo del fragment con la vista
    // private var _binding : CursoFrameBinding? = null     ????
    private lateinit var _binding : CursoFrameBinding

    // Variable de referencia al servidor de firebase para obtener los valores
    private lateinit var basedatos : FirebaseDatabase
    // Referencia al link del curso actual en firebase
    private lateinit var cursoActual : DatabaseReference
    // Referencia al link de la carpeta con el Almacenamiento Multimedia
    // TODO no estoy muy seguro de si esto funciona
    private lateinit var directorioAlmacenamiento : DatabaseReference

    // Override del metodo que crea la vista
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Enlazamos la clase Kotlin con la vista xml
        // _binding = CursoFrameBinding.inflate(inflater, container, false)
        // TODO esto es la variable de binding que solo existe en la sesion actual, solo entre onCreate y onDestroy, pero aun no se como funciona
        // val root : View = binding.root

        // Enlazamos esta clase a la vista xml que hemos creado
        return inflater.inflate(R.layout.curso_frame, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO recomienda no poner barras bajas, igual es mejor usar camelcase
        // Primero guardamos las Views de la vista en variables
        val localidad_curso: TextView = view.findViewById(R.id.localidad_curso)
        val imagen_curso: ImageView = view.findViewById(R.id.imagen_curso)
        val titulo_curso: TextView = view.findViewById(R.id.titulo_curso)
        val descripcion_curso : TextView = view.findViewById(R.id.descripcion_curso)
        val ubicacion_curso : TextView = view.findViewById(R.id.ubicacion)
        val profesor_curso : TextView = view.findViewById(R.id.profesor)
        val contacto_curso : TextView = view.findViewById(R.id.contacto)
        val boton_apuntarse : Button = view.findViewById(R.id.boton_apuntarse)

        // Inicializamos el Firebase, y decimos a que curso pertenece especificamente (TODO el que haya clickado el usuario, esto vendrá en el enlace o en el GET, o en el session)
        basedatos = FirebaseDatabase.getInstance()
        // TODO NOTA RAUL: Si no funciona esto, poner .child(Curso).child(C_001)
        cursoActual = basedatos.reference.child("Curso/C_001")
        directorioAlmacenamiento = basedatos.reference.child("ImagenesCursos")

        // Ponemos los valores de la base de datos en los objetos de la vista
        cursoActual.addValueEventListener(object : ValueEventListener {
            // definimos lo que pasa cuando cambian los datos en la base de datos (a tiempo real)
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Asignamos los valores del curso en base de datos al curso actual
                    // TODO Tambien podemos crear aqui una clase con esos atributos, y mantenerla, que no es a tiempo real, pero es integro
                    // TODO Meter la localidad actual en la que este el usuario, del atributo session
                    val localidad = snapshot.child("ubicacion").value.toString()
                    // La imagen del curso se pone en un listener sobre la carpeta de almacenamiento
                    val titulo = snapshot.child("titulo").value.toString()
                    val descripcion = snapshot.child("descripcion").value.toString()
                    val ubicacion = snapshot.child("ubicacion").value.toString()
                    val profesor = snapshot.child("profesor").value.toString()
                    val contacto = snapshot.child("contacto").value.toString()

                    // Se asignan los valores en las variables a los objetos de la vista
                    localidad_curso.text = localidad
                    titulo_curso.text = titulo
                    descripcion_curso.text = descripcion
                    ubicacion_curso.text = ubicacion
                    profesor_curso.text = profesor
                    contacto_curso.text = contacto
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Manejamos los errores de los datos en curso?")
            }
        })

        // TODO esto puede no funcionar: el enlace de la carpeta de Storage es diferente al de las tablas en tiempo real
        directorioAlmacenamiento.addValueEventListener(object : ValueEventListener {
            // aqui definimos solo que la imagen vaya cambiando, pues esta en una carpeta diferente
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val imagen = snapshot.child("AjedrezImg.png")

                    // TODO aqui hay que hacer un set a la imagen para poner la que acabamos de obtener
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Manejamos los errores de la imagen?")
            }
        })

        // TODO terminar esto
        // Definimos la funcion que se realiza al pulsar el boton, en este metodo, porque lo ponemos una vez esta creada la vista
        boton_apuntarse.setOnClickListener {
            TODO("Meter aqui que se pase a la pantalla de apuntarse, y meter la sesion actual y usuario y tal")
            // Pedimos al NavHostFragment que busque el fragmento de navegacion asociado a esta clase, y que navegue hacia otra pantalla mediante la accion definida en el navhostfragment
            NavHostFragment.findNavController(CursoFragment.this).
        }
    }

    // TODO esto funciona pero comporbar bien como usarlo, que no le veo utilidad
    // Funcion para obtener el valor de una clave de Curso
    fun getValorPorClave(clave: String) {
        cursoActual.child(clave).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val valor = snapshot.value.toString()
                // Aqui tenemos el valor que queremos, y lo usamos como se necesite
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Manejamos los errores?")
            }
        })
    }
}