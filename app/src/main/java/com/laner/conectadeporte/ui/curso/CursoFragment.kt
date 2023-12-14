package com.laner.conectadeporte.ui.curso

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.laner.conectadeporte.R
import com.laner.conectadeporte.databinding.CursoFrameBinding

class CursoFragment : Fragment() {

    // Variable del propio bindeo del fragment con la vista
    private var _binding : CursoFrameBinding? = null

    // Override del metodo que crea la vista
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Enlazamos la clase Kotlin con la vista xml
        _binding = CursoFrameBinding.inflate(inflater, container, false)
        // TODO esto es la variable de binding que solo existe en la sesion actual, solo entre onCreate y onDestroy, pero aun no se como funciona
        // val root : View = binding.root

        // Se van obteniendo los obj de la vista en variables
        // TODO terminar esto que estoy cansado
        
    }
}