package com.laner.conectadeporte.ui.curso

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.laner.conectadeporte.R
import com.laner.conectadeporte.databinding.FragmentHomeAyuntamientoBinding

class CursoAyuntamientoFragment : Fragment() {

    private lateinit var binding: FragmentHomeAyuntamientoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentHomeAyuntamientoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.iconoSalir.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_nav_curso_ayuntamientos_to_log)
        }
    }
}