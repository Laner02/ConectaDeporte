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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ValueEventListener
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

            if (validarCadena(telefono)) {
                println("La cadena es válida.")
            } else {
                println("La cadena no es válida.")
            }


            if (nombre.isNotEmpty() && apellido.isNotEmpty() && telefono.isNotEmpty() &&
                email.isNotEmpty() && contrasena.isNotEmpty() && contrasenaRep.isNotEmpty() ) {

                if (contrasena == contrasenaRep) {

                    firebaseAuth.createUserWithEmailAndPassword(email, contrasena)
                        .addOnCompleteListener(requireActivity()) {

                            if (it.isSuccessful) {
                               /* val intent = Intent(requireContext(), LogInFragment::class.java)
                                startActivity(intent)*/

                                val referenciaNodoPrincipal  = database.getReference("Usuario")

                               // Agrega un event listener para escuchar cambios en el nodo principal
                                referenciaNodoPrincipal.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                                        val user = hashMapOf(
                                            "nombre" to nombre,
                                            "apellidos" to apellido,
                                            "email" to email,
                                            "telefono" to telefono
                                        )

                                        // Obtiene el número de hijos
                                        val numeroDeHijos = dataSnapshot.childrenCount
                                        val num = numeroDeHijos + 1
                                     //   val emailModificada = num.toString() + "-" + email.replace('.', '-')

                                        directorioAlmacenamiento.child("Usuario").child(num.toString()).setValue(user)

                                        println("Número de hijos: $numeroDeHijos")
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        println("Error al obtener datos: ")
                                    }
                                })

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

        binding.contactanos.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.action_registrarse_to_nav_contactenos)
        }

    }

    fun validarCadena(cadena: String): Boolean {
        // Comprobar longitud
        if (cadena.length != 9) {
            return false
        }

        // Comprobar que todos los caracteres son dígitos
        if (!cadena.all { it.isDigit() }) {
            return false
        }

        // Opcional: Comprobar si la cadena es convertible a Int (si es necesario)
        val numero: Int? = cadena.toIntOrNull()
        if (numero == null) {
            return false
        }

        // Todas las comprobaciones pasaron, la cadena es válida
        return true
    }

}
