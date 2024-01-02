package com.laner.conectadeporte

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import com.laner.conectadeporte.databinding.ActivityMainBinding
import com.laner.conectadeporte.databinding.LogInBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       /* setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        */
    }

    // Codigo que se encarga de crear el menu lateral
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    /* SECCION MENU LATERAL */

    // El sistema llama a esta funcion cada vez que se selecciona un item del menu lateral.
    // Se pasa el item seleccionado
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Obtenemos el id del item seleccionado, y devolvemos dependiendo de cual sea una cosa u otra
        return when (item.itemId) {
            R.id.nav_perfil -> {
                accesoPerfil()
                true
            }
            R.id.nav_cursosApuntados -> {
                accesoCursosApuntados()
                true
            }
            R.id.nav_modoOscuro -> {
                activaModoOscuro()
                true
            }
            R.id.nav_cerrarSesion -> {
                cierraSesion()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Metodo que se encarga de pasar a la pantalla del perfil de usuario
    fun accesoPerfil() {
        // TODO Obtenemos el usuario desde SharedPrefs para pasarlo a la pagina del perfil
        val bundle = Bundle()
        // bundle.putString("usuarioActual", Sharedprefs.getString())
        bundle.putString("usuarioActual", "Raul")

        // Obtenemos el fragmento actual
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)

        // TODO Debug de control
        if (currentFragment == null)
            Log.w("[MainActivity]", "El fragmento actual es NULO.")

        // Poner la navegacion desde cualquier fragmento a las opciones de menu por si acaso
        NavHostFragment.findNavController(currentFragment!!).navigate(R.id.action_to_perfil, bundle)
    }

    fun accesoCursosApuntados() {
        // TODO FALTA POR IMPLEMENTAR, ES IGUAL QUE LA DE ARRIBA PERO DISTINTA ACCION DISTINTO DESTINO
    }

    fun activaModoOscuro() {
        // TODO FALTA POR IMPLEMENTAR, CAMBIA EL TEMA DE CLARO A OSCURO Y YA
    }

    fun cierraSesion() {
        // TODO FALTA POR IMPLEMENTAR, QUITA EL USUARIO ACTUAL DEL SHAREDPREFS, CIERRA SESION EN FIREBASE SI SE PUEDE, Y VUELVE A LA PANTALLA DE LOGIN DESTRUYENDO TODAS LAS DEMAS
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}