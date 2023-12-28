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
import com.laner.conectadeporte.R
import com.laner.conectadeporte.databinding.CardBlueprintBinding
import com.laner.conectadeporte.databinding.FragmentHomeBinding

// NOTA: Este codigo de creacion esta hecho automaticamente por AndoidStudio
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
        // TODO meter aqui texto localidad y cambiar la primera tarjeta
        val card1 : CardView = binding.card1
        // Prueba para convertir la carta personalizada a una Vista general para poderle poner un listener
        // Pillamos la carta de la vista directamente desde la referencia al constraintlayout
        val card2 : View = _binding!!.root.findViewById(R.id.card2)
        // val card2 : CardBlueprintBinding = binding.card2
        val card3 : CardBlueprintBinding = binding.card3
        val card4 : CardBlueprintBinding = binding.card4

        // TODO meter en un array de CardBluePrint todas las cartas, e ir recorriendolas para ponerles un onclicklistener a todas
        // TODO eso o creamos una clase que ya tenga ese onclick en ella y el bindeo a us respectiva tarjeta?

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