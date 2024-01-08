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
import com.laner.conectadeporte.databinding.LogInBinding


class LogInFragment : Fragment() {

    private lateinit var binding: LogInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var databaseRef : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = LogInBinding.inflate(inflater, container, false)
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

                            database = FirebaseDatabase.getInstance()

                            databaseRef = database.reference

                            val usersRef = databaseRef.child("Usuario")
                            var userId : Int = 0

                            val sharedPrefs = requireActivity().getPreferences(Context.MODE_PRIVATE)

                            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (userSnapshot in dataSnapshot.children) {

                                        val correo = userSnapshot.child("email").getValue(String::class.java)

                                        if(correo == email){
                                            userId = userSnapshot.key!!.toInt()
                                            //Log.v("[Usuario]", "Id " + userId )

                                            with(sharedPrefs.edit()) {
                                                putInt("usuarioActual", userId)
                                                putString("correoActual", correo)
                                                apply()
                                            }
                                            userId = sharedPrefs.getInt("usuarioActual", 0)

                                            // TODO prueba
                                            // Llamamos al metodo de cambiar cabecera del main activity
                                            (activity as MainActivity?)!!.cambiarCabecera()

                                            break

                                        }
                                        //Log.v("[Usuario]", "correo " + correo )
                                    }
                                }
                                override fun onCancelled(databaseError: DatabaseError) {
                                    Toast.makeText(requireContext(), "Error obteniendo informacion del usuario", Toast.LENGTH_SHORT).show()
                                }
                            })

                            NavHostFragment.findNavController(this).navigate(R.id.action_login_to_home)
                        } else {
                            Toast.makeText(requireContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        binding.botonInvitado.setOnClickListener{

            firebaseAuth.signInAnonymously()
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Inicio de sesión anónimo exitoso", Toast.LENGTH_SHORT).show()
                        val sharedPreferen = requireActivity().getPreferences(Context.MODE_PRIVATE)
                        val userId = sharedPreferen.getInt("usuarioActual", 0)
                        Log.v("[]","usuario: $userId")
                        NavHostFragment.findNavController(this).navigate(R.id.action_login_to_home)

                    } else {
                        Toast.makeText(requireContext(), "Error al iniciar sesion anonimamente", Toast.LENGTH_SHORT).show()
                    }
                }

        }

        binding.contactanos.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.action_log_to_nav_contactenos)
        }

    }

}
