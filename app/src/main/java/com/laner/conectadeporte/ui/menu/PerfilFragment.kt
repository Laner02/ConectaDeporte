package com.laner.conectadeporte.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.laner.conectadeporte.R
import com.laner.conectadeporte.databinding.PerfilFrameBinding
import com.laner.conectadeporte.src.Usuario

class PerfilFragment : Fragment() {

    // Variable de bindeo con la vista
    private var _binding : PerfilFrameBinding? = null
    private val binding get() = _binding!!

    // Variables de referencia al servidor de Firebase
    private lateinit var basedatos : FirebaseDatabase
    private lateinit var basedatosRef : DatabaseReference

    // Variables para obtener el usuario que se pasa por bundle TODO QUITARLO, SE PUEDE PILLAR DESDE EL SHAREDPREFS
    private var usuarioId : String? = null

    // Variable para guardar el usuario actual
    private lateinit var usuarioActual : Usuario

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Obtenemos el nombre de usuario que nos dan TODO QUITAR ESTO CUANDO SE HAGA LO DE ARRIBA
        usuarioId = arguments?.getString("usuarioActual")

        _binding = PerfilFrameBinding.inflate(inflater, container, false)

        return inflater.inflate(R.layout.perfil_frame, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Primero guardamos las View de la vista en variables
        val imagenPerfil : ImageView = binding.perfilImagenUsuario
        val nombreUsuario : TextView = binding.perfilNombreUsuario
        val correoUsuario : TextView = binding.perfilCorreoUsuario
        val nombreCompleto : TextView = binding.perfilNombreCompletoUsuario
        val correoContacto : TextView = binding.perfilCorreoContactoUsuario
        val telefonoContacto : TextView = binding.perfilTelefonoUsuario
        // TODO esta no me convece, o hacemos que retroceda al pulsar o lo quitamos, no quiero meter otra action
        val boton_atras : ImageView = view.findViewById(R.id.perfil_flechaAtras)

        basedatos = FirebaseDatabase.getInstance()
        basedatosRef = basedatos.reference

        // TODO SEGUIR, pillar los datos del usuario y metelos en una clase Usuario
    }
}