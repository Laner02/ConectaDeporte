package com.laner.conectadeporte.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment

import com.laner.conectadeporte.R
import com.laner.conectadeporte.databinding.LogInBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


enum class  ProviderType{
    BASIC
}

class LogInFragment : Fragment(){

    private lateinit var binding: LogInBinding
    private lateinit var auth: FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Enlazamos esta clase a la vista xml que hemos creado
        return inflater.inflate(R.layout.log_in, container, false)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usuario_email: EditText = view.findViewById(R.id.usuario_email)
        val usuario_contrasena: EditText = view.findViewById(R.id.usuario_contrasena)

        val boton_registrarse: AppCompatButton = binding.botonRegistrarse

        boton_registrarse.setOnClickListener() {

            val email = usuario_email.text.toString().trim()
            val contrasena = usuario_contrasena.text.toString()

            if (email.isNotEmpty() && contrasena.isNotEmpty()) {

                // Iniciar sesión con Firebase Authentication
                auth.signInWithEmailAndPassword(email, contrasena)
                    .addOnCompleteListener(requireActivity()) { task ->

                        if (task.isSuccessful) {

                            showNextLayout(task.result?.user?.email ?: "", ProviderType.BASIC)

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

    private fun showNextLayout(email: String, provider: ProviderTypeSalida){
        val homeIntent = Intent(this, salidaFragment::class.java).apply{
            putExtra("email", email)
            putExtra("provider", provider.name)
            startActivity(homeIntent)
        }

    }

    private fun updateUI(user: FirebaseUser?){

    }
}
}


