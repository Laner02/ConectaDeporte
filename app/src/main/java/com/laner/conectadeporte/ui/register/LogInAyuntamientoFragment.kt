package com.laner.conectadeporte.ui.register

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.laner.conectadeporte.MainActivity
import com.laner.conectadeporte.R
import com.laner.conectadeporte.databinding.LogInAyuntamientoBinding
import com.laner.conectadeporte.src.Ubicacion

class LogInAyuntamientoFragment : Fragment() {

    private lateinit var binding: LogInAyuntamientoBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var databaseRef : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = LogInAyuntamientoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.botonAcceder.setOnClickListener {
            val email = binding.usuarioEmail.text.toString()
            val contrasena = binding.usuarioContrasena.text.toString()

            if (email.isNotEmpty() && contrasena.isNotEmpty()) {

                val nombre_completo = email.split("@")[0]
                Log.v("[Ayuntamiento]", "nombre completo $nombre_completo" )
                val dominio = email.split("@")[1]

                var ayuntamiento : Boolean = false
                val nombre_primero : String

                if (nombre_completo.contains("_"))
                {
                    nombre_primero = nombre_completo.split("_")[0]
                    val nombre_segundo = nombre_completo.split("_")[1]

                    for(enum in Ubicacion.values()) {
                        val lugar = (enum.stringValue).replace(" ", "").lowercase()

                        if (nombre_segundo == lugar) {
                            ayuntamiento = true
                        }
                    }
                }
                else{
                    nombre_primero = nombre_completo
                }

                if(nombre_primero=="ayuntamiento" && dominio=="cdeporte.com" && ayuntamiento) {

                    // Iniciar sesión con Firebase Authentication
                    firebaseAuth.signInWithEmailAndPassword(email, contrasena)
                        .addOnCompleteListener(requireActivity()) { task ->

                            if (task.isSuccessful) {
                                NavHostFragment.findNavController(this).navigate(R.id.action_nav_log_in_ayuntamiento_to_nav_curso_ayuntamientos)
                            } else {
                                Toast.makeText(requireContext(),"Ayuntamiento no encontrado",Toast.LENGTH_SHORT).show()
                            }
                        }
                }else {
                    Toast.makeText(requireContext(), "Error al iniciar sesion", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(requireContext(), "Algun dato esta incompleto", Toast.LENGTH_SHORT).show()
            }
        }


        binding.contactanos.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.action_nav_log_in_ayuntamiento_to_nav_contactenos)
        }

    }
}