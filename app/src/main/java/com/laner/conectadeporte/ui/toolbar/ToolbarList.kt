package com.laner.conectadeporte.ui.toolbar

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.laner.conectadeporte.R
import com.laner.conectadeporte.databinding.ToolbarListBinding
import com.laner.conectadeporte.src.Ubicacion
import com.laner.conectadeporte.src.UbicacionCompleta

class ToolbarList : Fragment(){

    private var _binding :  ToolbarListBinding? = null
    private val binding get() = _binding!!

    private lateinit var listaFiltrada: MutableList<String>
    private lateinit var listView: ListView
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ToolbarListBinding.inflate(inflater,container,false)
        return inflater.inflate(R.layout.toolbar_list, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbarB : View = view.findViewById(R.id.busqueda_toolbar)
        val boton_perfil : ImageView = toolbarB.findViewById<ImageView>(R.id.icono_perfil)

        listaFiltrada = mutableListOf()

        for (enum in UbicacionCompleta.values()) {

            listaFiltrada.add(enum.stringValue)
        }
        Log.w("[ListaFiltrada]", "Lista: " + listaFiltrada)

        listView = view.findViewById(R.id.lista_view)
        searchView = view.findViewById(R.id.search_bar)


        arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listaFiltrada)
        listView.adapter = arrayAdapter

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                arrayAdapter.filter.filter(newText)
                Log.v("[BUSQUEDA]", "Buscar ciudad " + newText )
                return true
            }
        })

        listView.setOnItemClickListener{ parent, view, position, id ->
            val elementoClicado = listaFiltrada[position].substring(0, 4)

            // Cambiamos la localidad actual por la seleccionada en toda la app
            val sharedPrefs = requireActivity().getPreferences(Context.MODE_PRIVATE)
            with(sharedPrefs.edit()) {
                putString("localidadActual", elementoClicado)
                apply()
            }
            NavHostFragment.findNavController(this).navigate(R.id.action_nav_lista_busqueda_to_nav_home)
        }

        boton_perfil.setOnClickListener {
            // Obtenemos el drawer menu pidiendoselo a la main activity
            val drawerLayout : DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)
            drawerLayout.openDrawer(GravityCompat.END)
        }

    }
}