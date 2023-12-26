package com.laner.conectadeporte.ui.register

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.laner.conectadeporte.R

enum class  ProviderTypeSalida{
    BASIC
}
class salidaFragment: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.salida)

        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        setup(email?:"", provider?:"")
    }

    private fun setup(email: String, provider: String){
        emailTextView.text = email

        providerTextView.text = provider

        cerrarSesionButton.setOnClick
    }

}