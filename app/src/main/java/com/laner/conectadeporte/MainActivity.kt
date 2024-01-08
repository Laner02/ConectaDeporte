package com.laner.conectadeporte

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.laner.conectadeporte.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    // Variables de Firebase
    private lateinit var basedatos : FirebaseDatabase
    private lateinit var basedatosRef : DatabaseReference

    private var userId : Int = 0
    private lateinit var correoUser : String
    private lateinit var username : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Activamos la toolbar de la aplicacion
        val toolbar = binding.appBarMain.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.hide()

        // Activamos el menu drawe (menu lateral)
        val drawer: DrawerLayout = binding.drawerLayout

        // Creamos un listener para detectar los item que se seleccionan en el menu
        val navView: NavigationView = binding.navView

        navView.setNavigationItemSelectedListener {
            // En funcion del item seleccionado, se realiza una accion u otra
            when (it.itemId) {
                R.id.nav_miPerfil -> {
                    accesoPerfil()
                }
                R.id.nav_cursosApuntados -> {
                    accesoCursosApuntados()
                }
                R.id.nav_modoOscuro -> {
                    activaModoOscuro()
                }
                R.id.nav_cerrarSesion -> {
                    cierraSesion()
                }
                else -> super.onOptionsItemSelected(it)
            }
            // Se cierra el menu lateral por la izquierda
            val drawerLayout: DrawerLayout = binding.drawerLayout
            drawerLayout.closeDrawer(GravityCompat.END)
            // Se desactiva la opcion pulsada
            it.isChecked = false
            true
        }

        // Si existe el nombre del usuario en el SharedPrefs, lo ponemos en la cabecera
        val sharedPrefs = this.getPreferences(Context.MODE_PRIVATE)
        userId = sharedPrefs.getInt("usuarioActual", 0)
        correoUser = sharedPrefs.getString("correoActual", "correo")!!

        // TODO SI PILLA UN USUARIO, SALTA A LA PAGINA DE MAIN, SIN LOGIN

        val vista = navView.getHeaderView(0)
        val nombreUsuario : TextView = vista.findViewById(R.id.nombre_usuario)
        val correoUsuario : TextView = vista.findViewById(R.id.correo_usuario)

        if (userId == 0) {
            nombreUsuario.text = "Anonimo"
            correoUsuario.text = ""
        }

        else {
            basedatos = FirebaseDatabase.getInstance()
            basedatosRef = basedatos.reference

            basedatosRef.child("Usuario").child(userId.toString()).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        username = snapshot.child("nombre").value.toString()
                        Log.v("[Usuario]", "El usuario es: " + username)
                        nombreUsuario.text = correoUser
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar errores
                }
            })
        }
    }

    // Metodo que se encarga de pasar a la pantalla del perfil de usuario
    fun accesoPerfil() {
        Log.v("[AccesoPerfil]", "Accediendo al perfil...")

        // Obtenemos el fragmento actual
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)

        // Debug de control
        if (currentFragment == null)
            Log.w("[MainActivity]", "El fragmento actual es NULO.")

        // Poner la navegacion desde cualquier fragmento a las opciones de menu por si acaso
        NavHostFragment.findNavController(currentFragment!!).navigate(R.id.action_to_perfil)
        Log.v("[AccesoPerfil]", "Hecho!")
    }

    // TODO esto se podria reutilizar la de arriba que solo cambia la accion realizada, y optimizamos codigo
    fun accesoCursosApuntados() {
        // TODO Obtenemos el usuario desde SharedPrefs para pasarlo a la pagina del perfil
        val bundle = Bundle()
        bundle.putString("usuarioActual", username)

        // Obtenemos el fragmento actual
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)

        // Debug de control
        if (currentFragment == null)
            Log.w("[MainActivity]", "El fragmento actual es NULO.")

        // Poner la navegacion desde cualquier fragmento a las opciones de menu por si acaso
        NavHostFragment.findNavController(currentFragment!!).navigate(R.id.action_to_cursos_apuntados, bundle)
    }

    fun activaModoOscuro() {
        // TODO FALTA POR IMPLEMENTAR, CAMBIA EL TEMA DE CLARO A OSCURO Y YA
    }

    fun cierraSesion() {
        // TODO queda destruir el resto de pantallas, o la actual para que no pueda volver
        val sharedPrefs = this.getPreferences(Context.MODE_PRIVATE)
        /*val editor: SharedPreferences.Editor = sharedPrefs.edit()
        editor.remove("usuarioActual")
        editor.remove("correoActual")
        editor.apply()*/

        if (userId != 0) {
            with(sharedPrefs.edit()) {
                remove("usuarioActual")
                remove("correoActual")
                apply()
            }
        }

        // Obtenemos el fragmento actual
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)

        NavHostFragment.findNavController(currentFragment!!).navigate(R.id.action_to_login)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}