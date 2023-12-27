package com.laner.conectadeporte.ui.curso

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.laner.conectadeporte.R
import com.laner.conectadeporte.databinding.ApuntarseFrameBinding

class ApuntarseFragment : Fragment() {

    // Variable del propio bindeo del fragment con la vista
    private lateinit var _binding : ApuntarseFrameBinding

    // Variables de referencia al servidor de Firebase
    private lateinit var  basedatos : FirebaseDatabase
    private lateinit var cursoActual : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.apuntarse_frame, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Guardamos las View en variables
        val nombre_apuntarse = _binding.nombreUsuario
        val apellidos_apuntarse = _binding.apellidosUsuario
        val dni_apuntarse = _binding.dniUsuario
        val fnac_apuntarse = _binding.fnacUsuario
        val horario_apuntarse = _binding.horarioUsuario

        // Inicializamos el Firebase, y especificamos a que carpeta nos referimos
        basedatos = FirebaseDatabase.getInstance()
        // usuarioApuntadoActual = basedatos.reference.child("UsuarioApuntado")

        // TODO hola (terminar, meter boton de apuntarse, que crea el usuario apuntado, lo mete en la BD y vuelve al curso en cuestion)
    }
}