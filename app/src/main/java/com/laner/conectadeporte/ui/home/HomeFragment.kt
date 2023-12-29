package com.laner.conectadeporte.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
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

        // Incializamos la lista de cursos
        listaCursos = ArrayList()

        // Primero obtenemos los cursos de la localizacion actual desde la base de datos y los creamos
        basedatos = FirebaseDatabase.getInstance()
        basedatosRef = basedatos.reference

        basedatosRef.child("Curso").child(localidadActual.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Recorre los hijos de la Key con la localidad (los cursos)
                    for (childsnapshot in snapshot.children) {

                        var titulo_curso = childsnapshot.toString()
                        // Recorre los datos del curso actual
                        for (childCurso in childsnapshot.children) {
                            var contacto_curso = childCurso.child("contacto").value.toString()
                            val descripcion_curso = childCurso.child("descripcion").value.toString()
                            var precio_curso : Float? = childCurso.child("precio").getValue(Float::class.java)
                            val profesor_curso = childCurso.child("profesor").value.toString()
                            val ubicacion_curso = childCurso.child("ubicacion").value.toString()

                            // TODO El precio da un null pointer exception y no se por que, pero mientras el precio sera por defecto
                            if (precio_curso == null)
                                precio_curso = 40f

                            // Creamos el curso actual y lo anadimos a la lista de cursos   NOTA: ubicacion y localidad estan al reves uwu
                            var cursoActual = Course(titulo_curso,descripcion_curso,profesor_curso,ubicacion_curso,localidadActual,precio_curso!!)

                            listaCursos.add(cursoActual)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO Not yet implemented
            }
        })

        // TODO terminar esto para hacer las tarjetas recycle, y meter boyones hacia otras vistas

        // AQUI SE VAN PILLANDO LOS OBJETOS DE LA VISTA EN VARIABLES
        val localidadCurrente : TextView = binding.localidadActual
        // TODO hacer un override del metodo tostring en Ubicacion que devuelva el nombre entero no las siglas
        localidadCurrente.text = localidadActual.toString()
        // TODO cambiar la primera tarjeta
        val card1 : CardView = binding.card1

        // Variable inflater para meter las tarjetas
        val inflater = LayoutInflater.from(requireContext())

        // Vamos recorriendo el array de tarjetas, y las ponemos un onClickListener
        for (curso in listaCursos) {
            // Creamos la tarjeta y la metemos en la vista, y le ponemos el onclicklistener
            var tarjetaVista : View = inflater.inflate(R.layout.card_blueprint, binding.root, false)
            // Guardamos el id que se le dara a la tarjeta para referenciarla luego
            val idDinamico = ViewCompat.generateViewId()
            tarjetaVista.id = idDinamico
            // TODO prueba para cambiar la imagen
            tarjetaVista.findViewById<ImageView>(R.id.card_img).setImageResource(resources.getIdentifier(curso.getTitle() + ".png", "drawable", "com.laner.conectadeporte"))
            tarjetaVista.findViewById<TextView>(R.id.card_title).text = curso.getTitle()
            tarjetaVista.findViewById<TextView>(R.id.card_desc).text = curso.getDescription()

            // Metemos la nueva tarjeta dentro del scroll
            binding.mainScrollMenu.addView(tarjetaVista)

            // Definimos la accion que se ejecuta al pulsar la tarjeta  NOTA: esto no se si se le podia hacer antes de meterla en la vista o no
            binding.mainScrollMenu.findViewById<View>(idDinamico).setOnClickListener {
                // Creamos un objeto Bundle, en el que pasamos el titulo del curso para saber que curso es
                val bundle = Bundle()
                bundle.putString("cursoActual", curso.getTitle())
                bundle.putString("localidadActual", localidadActual.toString())

                // Pasamos el bundle con el titulo del curso
                NavHostFragment.findNavController(this).navigate(R.id.action_nav_home_to_nav_curso, bundle)
            }
        }

        // TODO eso o creamos una clase que ya tenga ese onclick en ella y el bindeo a us respectiva tarjeta?

        /*basedatosRef.child("Curso").child(localidadActual.toString()).addValueEventListener(object : ValueEventListener {
            // Se toman los datos de la base de datos, y se van creando las tarjetas segun se comprueba que aun queda un curso
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // TODO hacer un for que recorra todos los hijos de la localidad, y vaya creando una tarjeta con el include para cada una
                    // TODO luego ir insertando esas clases en la vista XML
                    // Hacer esto para cada hijo de la localidad, ir recorriendolos, tomar en una variable el cursoDB actual, e ir cambiando cosas
                    var card2DB : DataSnapshot = snapshot.child("Badminton La Rondilla")
                    binding.card2.cardTitle.text = card2DB.toString()
                    binding.card2.cardDesc.text = card2DB.child("descripcion").toString()
                    // TODO faltaria cambiar la imagen pero no se como hacerlo :P
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO Not yet implemented
            }
        })*/

        card1.setOnClickListener {
            // Se crea un objeto Bundle en el que se mete el curso especifico al que se accede
            val bundle = Bundle()
            bundle.putString("cursoActual", "Club Ajedrez")
            bundle.putString("localidadActual", localidadActual.toString())

            NavHostFragment.findNavController(this).navigate(R.id.action_nav_home_to_nav_curso)
        }
    }

    // TODO esto ponerlo en el resto de backs, para que destruya la pantalla si se cierra la app o si se cambia de pantalla
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}