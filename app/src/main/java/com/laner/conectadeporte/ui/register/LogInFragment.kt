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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.laner.conectadeporte.R
import com.laner.conectadeporte.databinding.LogInBinding


enum class  ProviderType{
    BASIC
}

class LogInFragment : Fragment(){

    private lateinit var binding: LogInBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in)
    }

    private fun setup(){

       /* val usuario_email: EditText  = View.findViewById(R.id.usuario_email)
        val usuario_contrasena: EditText  = view.findViewById(R.id.usuario_contrasena)

        */

        val boton_registrarse: AppCompatButton = binding.botonRegistrarse

        boton_registrarse.setOnClickListener(){
            val email = binding.usuarioEmail.text
            val contrasena = binding.usuarioContrasena.text

            if(email.isNotEmpty() && contrasena.isNotEmpty()){
                // Iniciar sesión con Firebase Authentication
                FirebaseAuth.signInWithEmailAndPassword(email, contrasena)
                    .addOnCompleteListener(requireActivity()) {
                        if (it.isSuccessful) {

                            showNextLayout(it.result?.user?.email?: "", ProviderType.BASIC)

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

}


