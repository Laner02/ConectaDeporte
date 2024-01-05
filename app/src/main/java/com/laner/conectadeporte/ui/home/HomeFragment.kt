package com.laner.conectadeporte.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.laner.conectadeporte.R
import com.laner.conectadeporte.databinding.CardBlueprintBinding
import com.laner.conectadeporte.databinding.FragmentHomeBinding
import com.laner.conectadeporte.src.Course
import com.laner.conectadeporte.src.Ubicacion

// Interfaz para esperar a que acabe de obtener los datos de la BD
// Tiene una funcion en caso de completarse y otra en caso de dar error
interface CursoFetchCallback {
    fun onCursosFetched(listaCursos: List<Course>)
    fun onFetchError(error: DatabaseError)
}

// NOTA: Este codigo de creacion esta hecho automaticamente por AndoidStudio
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Variables para la base de datos
    private lateinit var basedatos : FirebaseDatabase
    private lateinit var basedatosRef : DatabaseReference

    // TODO la localidad actual deberiamos ponerla de forma que se pueda cambiar desde la barra o el buscador. Desde fuera
    // Variables para guardar la localidad actual. La inicializamos en Valladolid por defecto
    private var localidadActual : Ubicacion = Ubicacion.VALL

    // Variable para almacenar todos los cursos que se pillen de la BD
    private lateinit var listaCursos : ArrayList<Course>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /* homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        } */
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO terminar esto para hacer las tarjetas recycle

        // Variables de los botones de la toolbar
        val toolbarH : View = view.findViewById(R.id.home_toolbar)
        val barra_busqueda : SearchView = toolbarH.findViewById(R.id.search_bar)
        val boton_perfil : ImageView = toolbarH.findViewById<ImageView>(R.id.icono_perfil)
        // AQUI SE VAN PILLANDO LOS OBJETOS DE LA VISTA EN VARIABLES
        val localidadCurrente : TextView = binding.localidadActual
        localidadCurrente.text = localidadActual.toString()
        // TODO hacer un override del metodo tostring en Ubicacion que devuelva el nombre entero no las siglas
        // TODO pero entonces deberiamos cambiar las localizaciones en la BD



        // TODO METE AQUI LOS BINDING ON CLICK LISTENER PARA LAS COSAS DE LA TOOLBAR, NO DESDE LA TOOLBAR
        binding.homeToolbar.iconoPerfil.setOnClickListener {
            Toast.makeText(requireContext(), "ImageView Clicked", Toast.LENGTH_SHORT).show()
            // llamamos al metodo que abre el menu lateral
            Log.v("[TOOLBAR]", "Se accede al metodo del icono")
            // Obtenemos el drawer menu pidiendoselo a la main activity
            val drawerLayout : DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)
            // DEBUG
            Log.w("[TOOLBAR]", "El drawer es nulo.")
            drawerLayout.openDrawer(GravityCompat.END)
        }

        boton_perfil.setOnClickListener {
            Toast.makeText(requireContext(), "ImageView Clicked", Toast.LENGTH_SHORT).show()
            // llamamos al metodo que abre el menu lateral
            Log.v("[TOOLBAR]", "Se accede al metodo del icono")
            // Obtenemos el drawer menu pidiendoselo a la main activity
            val drawerLayout : DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)
            // DEBUG
            Log.w("[TOOLBAR]", "El drawer es nulo.")
            drawerLayout.openDrawer(GravityCompat.END)
        }

        // Llamamos a la funcion de recuperar los cursos de la base de datos, y overrideamos
        // la funcion final de completado, para que se ejecute este codigo DESPUES de que se ejecute lo de la base de datos
        fetchCursos(object : CursoFetchCallback {
            // Override al metodo que se ejecuta despues de que se recupere la BD
            override fun onCursosFetched(listaCursos: List<Course>) {
                for (curso in listaCursos) {

                    // Variable inflater para meter las tarjetas
                    val inflater = LayoutInflater.from(requireContext())
                    val tarjetaVista : View = inflater.inflate(R.layout.card_blueprint, binding.root, false)

                    // TODO prueba para cambiar la imagen
                    // tarjetaVista.findViewById<ImageView>(R.id.card_img).setImageResource(resources.getIdentifier(curso.getTitle() + ".png", "drawable", "com.laner.conectadeporte"))
                    tarjetaVista.findViewById<TextView>(R.id.card_title).text = curso.getTitle()
                    tarjetaVista.findViewById<TextView>(R.id.card_desc).text = curso.getDescription()

                    // Metemos la nueva tarjeta dentro del scroll
                    binding.mainScrollMenu.addView(tarjetaVista)

                    // Le ponemos a la tarjeta un onclicklistener
                    tarjetaVista.setOnClickListener {
                        // Creamos una variable Bundle, para pasar al Fragment de Curso parametros
                        val bundle = Bundle()
                        bundle.putString("cursoActual", curso.getTitle())
                        bundle.putString("localidadActual", localidadActual.toString())
                        // NOTA RAUL: No se por que aqui pide un @HomeFragment, pero lo pide (._.)
                        NavHostFragment.findNavController(this@HomeFragment).navigate(R.id.action_nav_home_to_nav_curso, bundle)
                    }
                }
            }

            override fun onFetchError(error: DatabaseError) {
                // TODO Not yet implemented
            }

        })
    }

    // Funcion que obtiene los datos de los Cursos desde la BD, y crea clases Curso y los mete en una lista
    fun fetchCursos(callback: CursoFetchCallback) {

        // Primero obtenemos los cursos de la localizacion actual desde la base de datos y los creamos
        basedatos = FirebaseDatabase.getInstance()
        basedatosRef = basedatos.reference

        Log.v("[Localidad]", localidadActual.toString())

        basedatosRef.child("Curso").child(localidadActual.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                listaCursos = ArrayList()
                var cursos : ArrayList<Course> = ArrayList()
                if (snapshot.exists()) {
                    Log.v("[Snapshot]", "Localidad Actual: " + snapshot.key!!)
                    // Recorre los hijos de la Key con la localidad (los cursos)
                    for (childsnapshot in snapshot.children) {

                        Log.v("[Childsnapshot]", "Curso Actual: " + childsnapshot.key!!)
                        val titulo_curso = childsnapshot.key!!
                        // Recorre los datos del curso actual
                        var contacto_curso = childsnapshot.child("contacto").value.toString()
                        val descripcion_curso = childsnapshot.child("descripcion").value.toString()
                        val precio_curso : Float? = childsnapshot.child("precio").getValue(Float::class.java)
                        val profesor_curso = childsnapshot.child("profesor").value.toString()
                        val ubicacion_curso = childsnapshot.child("ubicacion").value.toString()

                        // Creamos el curso actual y lo anadimos a la lista de cursos   NOTA: ubicacion y localidad estan al reves uwu
                        val cursoActual = Course(titulo_curso,descripcion_curso,profesor_curso,ubicacion_curso,localidadActual,precio_curso!!)

                        listaCursos.add(cursoActual)
                        cursos.add(cursoActual)
                    }

                    // Manda el mensaje de que se ha completado la lista, y pasa la lista
                    callback.onCursosFetched(cursos)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO Not yet implemented
            }
        })
    }

    // TODO esto ponerlo en el resto de backs, para que destruya la pantalla si se cierra la app o si se cambia de pantalla
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}