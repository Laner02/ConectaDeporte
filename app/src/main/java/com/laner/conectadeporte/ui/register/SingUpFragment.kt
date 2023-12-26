package com.laner.conectadeporte.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import android.widget.EditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.laner.conectadeporte.R
import androidx.appcompat.app.AppCompatActivity
import com.laner.conectadeporte.databinding.RegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.laner.conectadeporte.databinding.LogInBinding

class RegisterFragment : Fragment() {

    private lateinit var binding: RegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var user : FirebaseUser
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Enlazamos esta clase a la vista xml que hemos creado
        return inflater.inflate(R.layout.register, container, false)
    }



    private fun setup(){

        val usuario_nombre: EditText  = view.findViewById(R.id.usuario_nombre)
        val usuario_apellido: EditText  = view.findViewById(R.id.usuario_apellido)
        val usuario_telefono: EditText  = view.findViewById(R.id.usuario_telefono)
        val usuario_id: EditText = view.findViewById(R.id.usuario_id)
        val usuario_contrasena: EditText  = view.findViewById(R.id.usuario_contrasena)
        val usuario_email: EditText  = view.findViewById(R.id.usuario_email)
        val boton_registrarse: AppCompatButton = view.findViewById(R.id.boton_registrarse)

        boton_registrarse.setOnClickListener(){
            val nombre = usuario_nombre.text.toString().trim()
            val apellido = usuario_apellido.text.toString().trim()
            val telefono = usuario_telefono.text.toString().trim()
            val nombreId = usuario_id.text.toString().trim()
            val contrasena = usuario_contrasena.text.toString().trim()
            val email = usuario_email.text.toString().trim()

            if(nombre.isNotEmpty() && apellido.isNotEmpty() && telefono.isNotEmpty() &&
                nombreId.isNotEmpty() && contrasena.isNotEmpty() && email.isNotEmpty()){

                auth.createUserWithEmailAndPassword(email, contrasena)
                    .addOnCompleteListener(requireActivity()) {

                        if (it.isSuccessful) {  // El email tiene que exitir

                            showNextLayout(it.result?.user?.email?: "", ProviderType.BASIC)

                            // Registro exitoso
                       /*     val user: FirebaseUser? = auth.currentUser
                            val userId = user?.uid

                            // Guardar nombre y apellido en la base de datos
                            if (userId != null) {
                                val usuarioMap = mapOf(
                                    "nombre" to nombre,
                                    "apellido" to apellido,
                                    "telefono" to telefono,
                                    "email" to email,
                                    "id" to nombreId
                                )

                                database.child("Usuario").child(userId).setValue(usuarioMap)
                            }
*/
                            // Puedes redirigir a la siguiente actividad o realizar otras acciones aquí
                        } else {
                            alerta()
                        }
                    }
            }
        }


    }

    private fun alerta(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showNextLayout(email: String, provider: ProviderType){
        val homeIntent = Intent(this, LogInFragment::class.java).apply{
            putExtra("email", email)
            putExtra("provider", provider.name)
            startActivity(homeIntent)
        }
    }

    private fun updateUI(user: FirebaseUser?){

    }
}