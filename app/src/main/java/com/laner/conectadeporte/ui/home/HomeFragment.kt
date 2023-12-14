package com.laner.conectadeporte.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

        // AQUI SE VAN PILLANDO LOS OBJETOS DE LA VISTA EN VARIABLES
        // val textView: TextView = binding.textHome
        val card1 : CardView = binding.card1
        /* homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        } */
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}