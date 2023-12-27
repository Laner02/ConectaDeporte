package com.laner.conectadeporte.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.laner.conectadeporte.databinding.SignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    private lateinit var binding: SignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

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

        binding.iniciarSesion.setOnClickListener {
            val intent = Intent(requireContext(), LogInFragment::class.java)
            startActivity(intent)
        }

        binding.botonRegistrarse.setOnClickListener() {

            val email = binding.usuarioEmail.text.toString()
            val contrasena = binding.usuarioContrasena.text.toString()
            val contrasenaRep = binding.usuarioContrasenaRep.text.toString()
            val nombre = binding.usuarioNombre.text.toString()
            val apellido = binding.usuarioApellido.text.toString()
            val telefono = binding.usuarioTelefono.text.toString()

            if (nombre.isNotEmpty() && apellido.isNotEmpty() && telefono.isNotEmpty() &&
                email.isNotEmpty() && contrasena.isNotEmpty() && contrasenaRep.isNotEmpty()
            ) {

                if (contrasena == contrasenaRep) {

                    firebaseAuth.createUserWithEmailAndPassword(email, contrasena)
                        .addOnCompleteListener(requireActivity()) {

                            if (it.isSuccessful) {
                                val intent = Intent(requireContext(), LogInFragment::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(requireContext(), "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
