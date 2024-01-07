package com.laner.conectadeporte.ui.menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.laner.conectadeporte.R
import com.laner.conectadeporte.databinding.PerfilFrameBinding
import com.laner.conectadeporte.src.Ubicacion
import com.laner.conectadeporte.src.Usuario

class PerfilFragment : Fragment() {

    // Variable de bindeo con la vista
    private var _binding : PerfilFrameBinding? = null
    private val binding get() = _binding!!

    // Variables de referencia al servidor de Firebase
    private lateinit var basedatos : FirebaseDatabase
    private lateinit var basedatosRef : DatabaseReference

    // Variables para obtener el usuario que se pasa por bundle TODO QUITARLO, SE PUEDE PILLAR DESDE EL SHAREDPREFS
    private var usuarioId : Int? = null

    // Variable para guardar el usuario actual
    private lateinit var usuarioActual : Usuario

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Obtenemos el usuario actual desde el SharedPrefs
        val sharedPrefs = requireActivity().getPreferences(Context.MODE_PRIVATE)
        usuarioId = sharedPrefs.getInt("usuarioActual", 0)

        if (usuarioId == 0)
            NavHostFragment.findNavController(this).navigate(R.id.action_to_home)


        _binding = PerfilFrameBinding.inflate(inflater, container, false)

        return inflater.inflate(R.layout.perfil_frame, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO PARA METER COSAS EN LAS VARIABLES DESDE LA BD, CON FINDVIEWBYID, CON BINDING NO FUNCIONA
        // Primero guardamos las View de la vista en variables
        val imagenPerfil : ImageView = view.findViewById(R.id.perfil_ImagenUsuario)
        val nombreUsuario : TextView = view.findViewById(R.id.perfil_nombreUsuario)
        val correoUsuario : TextView = view.findViewById(R.id.perfil_correoUsuario)
        val nombreCompleto : TextView = view.findViewById(R.id.perfil_nombreCompletoUsuario)
        val correoContacto : TextView = view.findViewById(R.id.perfil_correoContactoUsuario)
        val telefonoContacto : TextView = view.findViewById(R.id.perfil_telefonoUsuario)
        // TODO esta no me convece, o hacemos que retroceda al pulsar o lo quitamos, no quiero meter otra action
        val boton_atras : ImageView = view.findViewById(R.id.perfil_flechaAtras)

        boton_atras.setOnClickListener {
            // LLeva al usuario a la pagina anterior, imitando el boton de atras
            requireActivity().onBackPressed()
        }

        basedatos = FirebaseDatabase.getInstance()
        basedatosRef = basedatos.reference

        Log.w("[PERFIL]", "El usuario recibido es: " + usuarioId)

        basedatosRef.child("Usuario").child(usuarioId.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Log.v("PERFIL", "El snapshot recibido es: " + snapshot.toString())
                    // Pillamos los datos del usuario
                    val nombre = snapshot.child("nombre").value.toString()
                    val apellidos = snapshot.child("apellidos").value.toString()
                    val email = snapshot.child("email").value.toString()
                    val telefono = snapshot.child("telefono").value.toString()

                    // Creamos la clase usuario
                    usuarioActual = Usuario(email,nombre,apellidos,telefono)
                    // No metemos los cursos porque no los necesitamos

                    nombreUsuario.text = usuarioActual.getNombre()
                    correoUsuario.text = usuarioActual.getCorreo()
                    nombreCompleto.text = usuarioActual.getNombreCompleto()
                    correoContacto.text = usuarioActual.getCorreo()
                    telefonoContacto.text = usuarioActual.getTelefono()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("[CDERRORPerfil]", "Error obteniendo datos del usuario de la Base de Datos. Error: ${error.message}")
                // TODO mandar un toast de que ha habido un error??
            }
        })
    }
}