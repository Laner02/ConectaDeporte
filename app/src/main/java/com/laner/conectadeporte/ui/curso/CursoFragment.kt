package com.laner.conectadeporte.ui.curso

import android.os.Bundle
import android.util.Log
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
import com.laner.conectadeporte.src.Course
import com.laner.conectadeporte.src.Ubicacion
import java.io.File

class CursoFragment : Fragment() {

    // Variable del propio bindeo del fragment con la vista
    private var _binding : CursoFrameBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    // Variable de referencia binding a la vista, asertando que no es null la inicilizacion del _binding
    private val binding get() = _binding!!

    // Variable de referencia al servidor de firebase para obtener los valores
    private lateinit var basedatos : FirebaseDatabase
    // Referencia al link del curso actual en firebase
    private lateinit var basedatosRef : DatabaseReference
    // Referencia al link de la carpeta con el Almacenamiento Multimedia
    private lateinit var storage : DatabaseReference

    // titulo del curso de la pantalla actual, a nulo por defecto
    private var cursoId : String? = null
    private var localidadActual : String? = null

    // Variable para guardar el curso actual
    private lateinit var cursoActual : Course

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

        // Obtenemos el titulo del curso que nos llega por el objeto Bundle
        cursoId = arguments?.getString("cursoActual")
        localidadActual = arguments?.getString("localidadActual")

        // TODO inicializamos las variables binding a pesar de no usarlas solo para evitar errores fatales
        _binding = CursoFrameBinding.inflate(inflater, container, false)

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
        val precio_curso : TextView = view.findViewById(R.id.precio)
        val boton_apuntarse : Button = view.findViewById(R.id.boton_apuntarse)

        // Inicializamos el Firebase, y decimos a que curso pertenece especificamente (TODO el que haya clickado el usuario, esto vendrá en el enlace o en el GET, o en el session)
        basedatos = FirebaseDatabase.getInstance()
        basedatosRef = basedatos.reference

        // TODO eSTO DA NULL POINTER EXCEPTION, PERO CON EL PRIMER BOTON
        // Ponemos los valores de la base de datos en los objetos de la vista
        basedatosRef.child("Curso").child(localidadActual!!).child(cursoId!!).addValueEventListener(object : ValueEventListener {
            // definimos lo que pasa cuando cambian los datos en la base de datos (a tiempo real)
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Asignamos los valores del curso en base de datos al curso actual
                    val localidad = localidadActual
                    // La imagen del curso se pone en un listener sobre la carpeta de almacenamiento
                    val titulo = snapshot.key!!
                    val descripcion = snapshot.child("descripcion").value.toString()
                    val ubicacion = snapshot.child("ubicacion").value.toString()
                    val profesor = snapshot.child("profesor").value.toString()
                    val contacto = snapshot.child("contacto").value.toString()
                    val precio : Float? = snapshot.child("precio").getValue(Float::class.java)

                    // Creamos la clase Curso con los datos recibidos
                    cursoActual = Course(titulo, descripcion, profesor, ubicacion, Ubicacion.valueOf(localidadActual!!), precio!!)

                    // TODO meter en la clase los horarios y toda la vaina, y mostrarlos de alguna forma

                    // TODO tomar los valores de la clase cursoActual
                    // Se asignan los valores en las variables a los objetos de la vista
                    localidad_curso.text = localidadActual
                    titulo_curso.text = titulo
                    descripcion_curso.text = descripcion
                    ubicacion_curso.text = ubicacion
                    profesor_curso.text = profesor
                    contacto_curso.text = contacto
                    precio_curso.text = precio.toString() + " €"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("[CDERROR]", "Error obteniendo datos del curso de la Base de Datos. Error: ${error.message}")
            }
        })



        // TODO esto puede no funcionar: el enlace de la carpeta de Storage es diferente al de las tablas en tiempo real
        basedatosRef.child("ImagenesCursos").addValueEventListener(object : ValueEventListener {
            // aqui definimos solo que la imagen vaya cambiando, pues esta en una carpeta diferente
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val imagen = snapshot.child(cursoId!! + ".png")

                    // TODO terminar esto, probar a descargarlo en drawables y pillar el ID
                    var imagenTmp = File.createTempFile("imagen", "png")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO Manejamos los errores de la imagen?
            }
        })

        // Definimos la funcion que se realiza al pulsar el boton, en este metodo, porque lo ponemos una vez esta creada la vista
        boton_apuntarse.setOnClickListener {
            // Le pasamos a la pantalla de Apuntarse el titulo del curso al que se apunta el usuario,
            // para meterlo en la BD y relacionarlos
            val bundle = Bundle()
            bundle.putString("cursoApuntarse", cursoActual.getTitle())

            // Pedimos al NavHostFragment que busque el fragmento de navegacion asociado a esta clase, y que navegue hacia otra pantalla mediante la accion definida en el navhostfragment
            NavHostFragment.findNavController(this).navigate(R.id.action_registrarse_to_curso, bundle)
        }
    }
}