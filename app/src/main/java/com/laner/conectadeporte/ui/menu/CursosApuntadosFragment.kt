package com.laner.conectadeporte.ui.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.laner.conectadeporte.R
import com.laner.conectadeporte.databinding.CursosApuntadosFrameBinding
import com.laner.conectadeporte.src.Course
import com.laner.conectadeporte.src.Ubicacion
import com.laner.conectadeporte.src.Usuario
import kotlin.concurrent.fixedRateTimer

// Interfaz para recuperar los cursos del usuario para meterlos uno a uno
interface CursoApuntadoFetchCallback {
    fun onCursosFetched(listaCursosApuntados: List<Course>)
    fun onFetchError(error: DatabaseError)
}

class CursosApuntadosFragment : Fragment() {

    // Variables de bindeo con la vista
    private var _binding : CursosApuntadosFrameBinding? = null
    private val binding get() = _binding!!

    // Variables de referencia al servidor de Firebase
    private lateinit var basedatos : FirebaseDatabase
    private lateinit var basedatosRef : DatabaseReference

    // Variable para obtener el usuario que se pasa por bundle TODO esto pillarlo del SharedPrefs
    private var usuarioId : String? = null

    // variable para guardar el usuario actual
    private lateinit var usuarioActual : Usuario
    // Lista de cursos a los que esta apuntado el usuario
    private lateinit var listaCursosApuntados : ArrayList<Course>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Obtenemos el nombre de usuario que se pasa TODO esto quitarlo luego
        usuarioId = arguments?.getString("usuarioActual")

        _binding = CursosApuntadosFrameBinding.inflate(inflater, container, false)

        return inflater.inflate(R.layout.cursos_apuntados_frame, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Primero guardamos las View de la vista en variables
        val imagenPerfil : ImageView = view.findViewById(R.id.cursos_apuntados_ImagenUsuario)
        val nombreUsuario : TextView = view.findViewById(R.id.cursos_apuntados_nombreUsuario)
        val boton_atras : ImageView = view.findViewById(R.id.cursos_apuntados_flechaAtras)

        boton_atras.setOnClickListener {
            // LLeva al usuario a la pagina anterior, imitando el boton de atras
            requireActivity().onBackPressed()
        }

        basedatos = FirebaseDatabase.getInstance()
        basedatosRef = basedatos.reference

        basedatosRef.child("Usuario").child(usuarioId!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Pillamos los datos del usuario
                    val nombre = snapshot.child("nombre").value.toString()
                    val apellidos = snapshot.child("apellidos").value.toString()
                    val email = snapshot.child("email").value.toString()
                    val telefono = snapshot.child("telefono").value.toString()

                    // Buscamos los cursos a los que esta apuntado el usuario en otro dataChange

                    // Creamos el usuario y metemos sus cursos apuntados  luego
                    usuarioActual = Usuario(email,nombre,apellidos,telefono)

                    // TODO meter aqui el cambio de imagen si hay
                    nombreUsuario.text = "Cursos de " + nombre
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("[CDERRORCursosApuntados]", "Error obteniendo datos del usuario de la Base de Datos. Error: ${error.message}")
                // TODO mandar un toast de que ha habido un error??
            }
        })

        // TODO si no funciona esto, pillar solo los nombres y meterlos tal cual
        // Despues de obtener los datos de la BD, se ponen en la pantalla los cursos a los que esta apuntado el usuario
        fetchCursosApuntados(object : CursoApuntadoFetchCallback {
            // Override del metodo que se ejecuta despues de fetchCursosApuntados
            override fun onCursosFetched(listaCursosApuntados: List<Course>) {
                for (curso in listaCursosApuntados) {

                    // Variable inflater para meter las tarjetas
                    val inflater = LayoutInflater.from(requireContext())
                    val tarjetaVista : View = inflater.inflate(R.layout.card_blueprint, binding.root, false)

                    // TODO aqui tambien cambiar la imagen
                    tarjetaVista.findViewById<TextView>(R.id.card_title).text = curso.getTitle()
                    tarjetaVista.findViewById<TextView>(R.id.card_desc).text = curso.getDescription()

                    // Metemos la nueva tarjeta en el contenido
                    binding.cursosApuntadosContenido.addView(tarjetaVista)

                    // TODO LE PONEMOS A LA TARJETA UN BOTON??? ESTARIA GUAY
                }
            }

            override fun onFetchError(error: DatabaseError) {
                // TODO meter el control de errores
            }
        })

    }

    // Funcion que obtiene los datos de los Cursos a los que esta apuntado un usuario desde la BD
    fun fetchCursosApuntados(callback: CursoApuntadoFetchCallback) {

        basedatos = FirebaseDatabase.getInstance()
        basedatosRef = basedatos.reference

        // TODO de momento solo ponemos los de VALL, porque meter otro bucle for complica las cosas y no se como usar un mapa para arreglar esto
        basedatosRef.child("matricula").child("VALL").child(usuarioId!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Por si acaso mantenemos una lista que se devuelve y otra global
                listaCursosApuntados = ArrayList()
                var cursos : ArrayList<String> = ArrayList()
                if (snapshot.exists()) {
                    // Recorre los id de los cursos a los que esta apuntado el usuario
                    for (childsnapshot in snapshot.children) {
                        cursos.add(childsnapshot.key!!)
                    }

                    // Manda el mensaje de que se ha completado la lista
                    // TODO si no funciona la funcion, mandamos los nombres solo
                    // callback.onCursosFetched(cursos)
                    // Llama a otra funcion para que pille los cursos
                    callback.onCursosFetched(searchCursos(cursos))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO meter mensaje de error
            }
        })
    }

    // Funcion que busca los cursos de la lista recibida en la BD
    fun searchCursos(listaCursos : ArrayList<String>) : List<Course> {
        // TODO esto se podria pasar como parametro pero por evitar posibles problemas lo dejo para luego
        basedatos = FirebaseDatabase.getInstance()
        basedatosRef = basedatos.reference

        var cursos : ArrayList<Course> = ArrayList()

        for (cursoId in listaCursos) {
            basedatosRef.child("Curso").child("VALL").child(cursoId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Log.v("[CursoApuntado]", "Recuperando Curso Apuntado...")
                        val localidadActual = Ubicacion.valueOf(snapshot.key!!)
                        // Recorre los hijos de la Key con la localidad (los cursos)
                        for (childsnapshot in snapshot.children) {
                            val titulo_curso = childsnapshot.key!!
                            // Recorre los datos del curso actual
                            // TODO pillar la imagen tambien
                            var contacto_curso = childsnapshot.child("contacto").value.toString()
                            val descripcion_curso = childsnapshot.child("descripcion").value.toString()
                            val precio_curso : Float? = childsnapshot.child("precio").getValue(Float::class.java)
                            val profesor_curso = childsnapshot.child("profesor").value.toString()
                            val ubicacion_curso = childsnapshot.child("ubicacion").value.toString()

                            // Creamos el curso actual y lo anadimos a la lista de cursos   NOTA: ubicacion y localidad estan al reves uwu
                            val cursoActual = Course(titulo_curso,descripcion_curso,profesor_curso,ubicacion_curso,localidadActual,precio_curso!!)

                            cursos.add(cursoActual)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("[CDERRORCursosApuntadosCurso]", "Error obteniendo datos del curso de la Base de Datos. Error: ${error.message}")
                }

            })
        }

        return cursos
    }

    // TODO meter al final el textview con apuntate a un curso de X forma
}