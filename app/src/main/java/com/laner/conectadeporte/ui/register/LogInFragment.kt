package com.laner.conectadeporte.ui.register

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.Firebase
import com.laner.conectadeporte.databinding.LogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore
import com.laner.conectadeporte.R
import com.laner.conectadeporte.UserApp.Companion.prefs

class LogInFragment : Fragment() {

    private lateinit var binding: LogInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var directorioAlmacenamiento : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = LogInBinding.inflate(inflater, container, false)
        checkUserValues()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {0
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.registrarseLogIn.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_login_to_signup)
        }

        binding.botonAcceder.setOnClickListener {
            val email = binding.usuarioEmail.text.toString()
            val contrasena = binding.usuarioContrasena.text.toString()

            if (email.isNotEmpty() && contrasena.isNotEmpty()) {
                // Iniciar sesión con Firebase Authentication
                firebaseAuth.signInWithEmailAndPassword(email, contrasena)
                    .addOnCompleteListener(requireActivity()) { task ->

                        if (task.isSuccessful) {

                            // Obtenemos los SharedPrefs y metemos el usuario actual y la localidad actual
                            val sharedPrefs = requireActivity().getPreferences(Context.MODE_PRIVATE)
                            with(sharedPrefs.edit()) {
                                putInt("usuarioActual", 3)
                                putString("localidadActual", "VALL")
                                apply()
                            }

                            val bundle = Bundle()
                            bundle.putString("localidadActual", "VALL")
                            // TODO RAUL AQUI, cambia por el shared

                            NavHostFragment.findNavController(this).navigate(R.id.action_login_to_home, bundle)
                        } else {
                            Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        binding.botonInvitado.setOnClickListener{

            firebaseAuth.signInAnonymously()
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Inicio de sesión anónimo exitoso", Toast.LENGTH_SHORT).show()
                        NavHostFragment.findNavController(this).navigate(R.id.action_login_to_home)
                    } else {
                        Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

        }

    }

    fun checkUserValues() {

        if (prefs.getEmail().isNotEmpty()) {

            NavHostFragment.findNavController(this).navigate(R.id.action_login_to_home)
        }
    }

}
