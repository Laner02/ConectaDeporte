package com.laner.conectadeporte.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.laner.conectadeporte.databinding.SignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.laner.conectadeporte.R
import com.laner.conectadeporte.src.Usuario

class SignUpFragment : Fragment() {

    private lateinit var binding: SignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var directorioAlmacenamiento : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        directorioAlmacenamiento = database.reference

        binding.iniciarSesion.setOnClickListener {
            /*val intent = Intent(requireContext(), LogInFragment::class.java)
            startActivity(intent)*/
            // Solo vuelve a la pantalla de Login
            NavHostFragment.findNavController(this).navigate(R.id.action_signup_to_login)
        }

        binding.botonRegistrarse.setOnClickListener() {

            val email = binding.usuarioEmail.text.toString()
            val contrasena = binding.usuarioContrasena.text.toString()
            val contrasenaRep = binding.usuarioContrasenaRep.text.toString()
            val nombre = binding.usuarioNombre.text.toString()
            val apellido = binding.usuarioApellido.text.toString()
            val telefono = binding.usuarioTelefono.text.toString()

            if (nombre.isNotEmpty() && apellido.isNotEmpty() && telefono.isNotEmpty() &&
                email.isNotEmpty() && contrasena.isNotEmpty() && contrasenaRep.isNotEmpty() ) {

                if (contrasena == contrasenaRep) {

                    firebaseAuth.createUserWithEmailAndPassword(email, contrasena)
                        .addOnCompleteListener(requireActivity()) {

                            if (it.isSuccessful) {
                               /* val intent = Intent(requireContext(), LogInFragment::class.java)
                                startActivity(intent)*/

                                val user = hashMapOf(
                                    "nombre" to nombre,
                                    "apellidos" to apellido,
                                    "email" to email,
                                    "telefono" to telefono
                                )
                                directorioAlmacenamiento.child("Usuario").child(nombre).setValue(user)

                                NavHostFragment.findNavController(this).navigate(R.id.action_signup_to_login)
                            } else {
                                Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(requireContext(), "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Empty Fields Are not Allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
