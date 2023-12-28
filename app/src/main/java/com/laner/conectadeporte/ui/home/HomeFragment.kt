package com.laner.conectadeporte.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
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

        // TODO terminar esto para que se pillen de la BD las tarjetas, hacerlas recycle, y meter boyones hacia otras vistas

        // AQUI SE VAN PILLANDO LOS OBJETOS DE LA VISTA EN VARIABLES
        val localidadActual : TextView = binding.localidadActual
        // TODO cambiar la primera tarjeta
        val card1 : CardView = binding.card1
        // Prueba para convertir la carta personalizada a una Vista general para poderle poner un listener
        // Pillamos la carta de la vista directamente desde la referencia al constraintlayout
        val card2 : View = _binding!!.root.findViewById(R.id.card2)
        // val card2 : CardBlueprintBinding = binding.card2
        val card3 : CardBlueprintBinding = binding.card3
        val card4 : CardBlueprintBinding = binding.card4

        // TODO meter en un array de CardBluePrint todas las cartas, e ir recorriendolas para ponerles un onclicklistener a todas
        // TODO eso o creamos una clase que ya tenga ese onclick en ella y el bindeo a us respectiva tarjeta?

        basedatos = FirebaseDatabase.getInstance()
        basedatosRef = basedatos.reference

        basedatosRef.child("Curso").child(localidadActual.toString()).addValueEventListener(object : ValueEventListener {
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
        })

        // TODO terminar con el resto de cursos de la BD

        card1.setOnClickListener {
            // Se crea un objeto Bundle en el que se mete el curso especifico al que se accede
            NavHostFragment.findNavController(this).navigate(R.id.action_nav_home_to_nav_curso)
        }

        card2.setOnClickListener {
            // TODO si esto no funciona, crear una variable cursoActual en el SharedPrefs
            // Tratamos de obtener el texto del titulo dentro del include de la tarjeta
            val tituloCurso : TextView = card2.findViewById(R.id.card_title)

            // Creamos un objeto bundle en el que pasar las variables
            val bundle = Bundle()
            bundle.putString("cursoActual", tituloCurso.text.toString())

            // Pasamos el bundle con el titulo del curso
            NavHostFragment.findNavController(this).navigate(R.id.action_nav_home_to_nav_curso, bundle)
        }
    }

    // TODO esto ponerlo en el resto de backs, para que destruya la pantalla si se cierra la app o si se cambia de pantalla
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}