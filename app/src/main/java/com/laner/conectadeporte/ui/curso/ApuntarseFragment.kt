package com.laner.conectadeporte.ui.curso

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.database.*
import com.laner.conectadeporte.R
import com.laner.conectadeporte.databinding.ApuntarseFrameBinding

class ApuntarseFragment : Fragment() {

    // Variable del propio bindeo del fragment con la vista
    private var _binding : ApuntarseFrameBinding? = null
    private val binding get() = _binding!!

    // Variables de referencia al servidor de Firebase
    private lateinit var  basedatos : FirebaseDatabase
    private lateinit var basedatosRef : DatabaseReference

    private lateinit var cursoId : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Aqui es .toString()? o !!?
        cursoId = arguments?.getString("cursoApuntarse")!!

        // Incializamos la variable _binding
        _binding = ApuntarseFrameBinding.inflate(inflater, container, false)

        return inflater.inflate(R.layout.apuntarse_frame, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Guardamos las View en variables
        val nombre_apuntarse = binding.nombreUsuario
        val apellidos_apuntarse = binding.apellidosUsuario
        val dni_apuntarse = binding.dniUsuario
        val fnac_apuntarse = binding.fnacUsuario
        val horario_apuntarse = binding.horarioUsuario
        // Variables para botones para que funcione ponerles un onclicklistener
        val boton_apuntarse = binding.botonRegistrarse
        val boton_cancelar = binding.botonCancelar

        // Inicializamos el Firebase, y especificamos a que carpeta nos referimos
        basedatos = FirebaseDatabase.getInstance()
        basedatosRef = basedatos.reference

        // TODO creo que ponerlo con binding y no en una variable hace que no funcione
        boton_apuntarse.setOnClickListener {

            // TODO obtenemos el correo del usuario actual registrado, en una variable en SharedPreferences
            val correoUsuarioActual = "prueba@gmail.com"

            // Creamos un hashMap con los datos del usuario apuntado
            val usuarioApuntado = hashMapOf(
                "cursoApuntado" to cursoId,
                "nombreApuntado" to nombre_apuntarse.text.toString(),
                "apellidosApuntado" to apellidos_apuntarse.text.toString(),
                "fnacApuntado" to fnac_apuntarse.text.toString(),
                "horarioApuntado" to horario_apuntarse.text.toString()
            )

            // Metemos el usuario apuntado en la base de datos.
            // Se organizan en UsuarioApuntado > UsuarioAsociado > DNIApuntado, por si se apuntan varios dni desde una misma cuenta
            // TODO meter dni en un variable y comprobar que es un dni valido antes de meterlo
            basedatosRef.child("UsuarioApuntado").child(correoUsuarioActual).child(dni_apuntarse.text.toString()).setValue(usuarioApuntado)

            // La aplicacion vuelve a la pantalla anterior del curso
            // TODO ocultar o eliminar esta pantalla para que no pueda volver con la flecha hacia atras
            // TODO NO FUNCIONA BIEN SI VUELVE AL CURSO ANTERIOR, NECESITAMOS QUE VUELVA A LA PANTALLA PRINCIPAL
            NavHostFragment.findNavController(this).navigate(R.id.action_apuntarse_to_curso)
        }

        // Si el usuario pulsa el boton de cancelar, vuelve al curso sin crear el usuario
        boton_cancelar.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_apuntarse_to_curso)
        }

        // TODO RECUERDA MANEJAR ERROR DE LOS EDITTEXT, Y METEMOS TEXTO EN ROJO SI VEMOS ALGO MAL, LO DESTRUIMOS EN EL BOTON REGISTRASE
    }
}