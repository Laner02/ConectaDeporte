package com.laner.conectadeporte.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.laner.conectadeporte.databinding.LogInBinding
import com.laner.conectadeporte.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.laner.conectadeporte.R
import com.laner.conectadeporte.ui.home.HomeFragment

class LogInFragment : Fragment() {

    private lateinit var binding: LogInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        // Modificacion de Raul para que se pueda cambiar de fragment
        binding.registrarseLogIn.setOnClickListener {
            /*val intent = Intent(requireContext(), SignUpFragment::class.java)
            startActivity(intent)*/
            NavHostFragment.findNavController(this).navigate(R.id.action_login_to_signup)
        }

        // TODO PARA Sara: Poner esto con el acceso conectando a Firebase
        // Prueba de Raul para ver si puede funcionar esto
        binding.botonAcceder.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_login_to_home)
        }

        /*binding.botonAcceder.setOnClickListener {
            val email = binding.usuarioEmail.text.toString()
            val contrasena = binding.usuarioContrasena.text.toString()

            if (email.isNotEmpty() && contrasena.isNotEmpty()) {
                // Iniciar sesión con Firebase Authentication
                firebaseAuth.signInWithEmailAndPassword(email, contrasena)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(requireContext(), HomeFragment::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }*/
    }
}
