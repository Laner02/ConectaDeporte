package com.laner.conectadeporte.ui.toolbar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.laner.conectadeporte.R
import com.laner.conectadeporte.databinding.ToolbarFrameBinding
import com.laner.conectadeporte.src.Ubicacion

class Toolbar : Fragment() {
    // Variable del propio bindeo del fragment con la vista
    private  var _binding : ToolbarFrameBinding? = null
    private val binding get() = _binding!!

    // Variables de referencia al servidor de Firebase
    private lateinit var basedatos : FirebaseDatabase
    private lateinit var basedatosRef : DatabaseReference
    private lateinit var listaFiltrada: MutableList<String>
    private lateinit var listView: ListView
    private lateinit var searchView: SearchView
    private lateinit var arrayAdapter: ArrayAdapter<String>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ToolbarFrameBinding.inflate(inflater, container, false)

        return inflater.inflate(R.layout.toolbar_frame, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

     /*   for (enum in Ubicacion.values()) {

            listaFiltrada.add(enum.name)

            val inflater = LayoutInflater.from(requireContext())
            val ubicacionEtiqueta: View = inflater.inflate(R.layout.toolbar_list, binding.root, false)

            ubicacionEtiqueta.findViewById<View>(R.id.lista_ubicaciones) = enum.toString()

            binding.ubicacionLista.addView(ubicacionEtiqueta)

            tarjetaLista.setOnClickListener {
                // Creamos una variable Bundle, para pasar al Fragment de Curso parametros
                val bundle = Bundle()
                bundle.putString("Enum", enum.toString())

                NavHostFragment.findNavController(this@Toolbar).navigate(R.id.action_nav_home_to_nav_home, bundle)
            }


        }
        for (enum in Ubicacion.values()) {

            listaFiltrada.add(enum.name)
        }

        listView = view.findViewById(R.id.listView)
        searchView = view.findViewById(R.id.search_bar)


        arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listaFiltrada)
        listView.adapter = arrayAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.v("[BUSQUEDA]", "Buscar ciudad " + query )

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                arrayAdapter.filter.filter(newText)
                return true
            }
        })


        */





        // Guardamos en variables las 3 vistas de la toolbar
        val menu_localidades : ImageView = binding.toggleMenu
        // TODO esto sara mete lo que uses
        val barra_busqueda : androidx.appcompat.widget.SearchView = binding.searchBar
        val icono_usuario : ImageView = binding.iconoPerfil

        // Accion de abrir el drawer menu cuando se pulsa este boton
        icono_usuario.setOnClickListener {
            Log.v("[TOOLBAR]", "Se accede al metodo del icono")
            // Obtenemos el drawer menu pidiendoselo a la main activity
            val drawerLayout : DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)
            // DEBUG
            Log.w("[TOOLBAR]", "El drawer es nulo.")
            drawerLayout.openDrawer(GravityCompat.END)
        }

        binding.iconoPerfil.setOnClickListener {
            Log.v("[TOOLBAR]", "Se accede al metodo del icono")
            // Obtenemos el drawer menu pidiendoselo a la main activity
            val drawerLayout : DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)
            // DEBUG
            Log.w("[TOOLBAR]", "El drawer es nulo.")
            drawerLayout.openDrawer(GravityCompat.END)
        }
    }
}