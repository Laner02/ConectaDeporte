package com.laner.conectadeporte.ui.curso

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import com.google.firebase.database.*
import com.laner.conectadeporte.R
import com.laner.conectadeporte.databinding.ApuntarseFrameBinding

class ApuntarseFragment : Fragment() {

    // Variable del propio bindeo del fragment con la vista
    private var _binding : ApuntarseFrameBinding? = null
    private val binding get() = _binding!!

    // Variables de referencia al servidor de Firebase
    private lateinit var  basedatos : FirebaseDatabase
    private lateinit var basedatosRef : DatabaseReference

    private lateinit var cursoId : String
    private lateinit var localidadActual : String
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Aqui es .toString()? o !!?
        cursoId = arguments?.getString("cursoApuntarse")!!
        localidadActual = arguments?.getString("localidadActual")!!

        // Incializamos la variable _binding
        _binding = ApuntarseFrameBinding.inflate(inflater, container, false)

        return inflater.inflate(R.layout.apuntarse_frame, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Guardamos las View en variables
        val nombre_apuntarse: EditText = view.findViewById(R.id.apuntarse_nombre_usuario)
        val apellidos_apuntarse: EditText = view.findViewById(R.id.apellidos_usuario)
        val dni_apuntarse: EditText = view.findViewById(R.id.dni_usuario)
        val fnac_apuntarse: EditText = view.findViewById(R.id.fnac_usuario)
        var horario_seleccionado : String = ""
        //val horario_apuntarse = binding.horarioUsuario
        // Variables para botones para que funcione ponerles un onclicklistener
        val boton_apuntarse : AppCompatButton = view.findViewById(R.id.apuntarse_boton_apuntarse)
        val boton_cancelar : AppCompatButton = view.findViewById(R.id.boton_cancelar)


        val myTextView = view.findViewById<TextView>(R.id.horario_usuario)
        val spinner = view.findViewById<Spinner>(R.id.spinner_horario)

        val horario_curso = arrayOf("L/M  19-20h  14-16 años", "M/J  17-18h  17-19 años", "L/M  20-22h  19-22 años")
        arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, horario_curso)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        myTextView.setOnClickListener {
            spinner.performClick()
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, posicion: Int, id: Long) {
                horario_seleccionado = horario_curso[posicion]
                myTextView.text = horario_seleccionado
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


        // Inicializamos el Firebase, y especificamos a que carpeta nos referimos
        basedatos = FirebaseDatabase.getInstance()
        basedatosRef = basedatos.reference


        // TODO creo que ponerlo con binding y no en una variable hace que no funcione
        boton_apuntarse.setOnClickListener {
            if(horario_seleccionado == "") {
                horario_seleccionado = horario_curso[0]
            }

            if (comprobarDNI(dni_apuntarse.text.toString())) {

                val sharedPrefs = requireActivity().getPreferences(Context.MODE_PRIVATE)
                val usuarioId = sharedPrefs.getInt("usuarioActual", 0)

                Log.v("[Apuntarse]", "Usuario: $usuarioId")

                val referenciaNodoPrincipal  = basedatos.getReference("UsuarioApuntado")

                // TODO O hacemos un numero de ID, y el dni como atributo
                referenciaNodoPrincipal.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {


                    // Creamos un hashMap con los datos del usuario apuntado
                        val usuarioApuntado = hashMapOf(
                            "dniApuntado" to dni_apuntarse.text.toString(),
                            "cursoApuntado" to cursoId,
                            "nombreApuntado" to nombre_apuntarse.text.toString(),
                            "apellidosApuntado" to apellidos_apuntarse.text.toString(),
                            "fnacApuntado" to fnac_apuntarse.text.toString(),
                            "horarioApuntado" to horario_seleccionado
                        )


                        val numeroDeHijos = dataSnapshot.child(usuarioId.toString()).childrenCount

                        val num_dni = (numeroDeHijos + 1).toString() + " -> " + dni_apuntarse.text.toString()

                        // Se organizan en UsuarioApuntado > UsuarioAsociado > DNIApuntado, por si se apuntan varios dni desde una misma cuenta
                        basedatosRef.child("UsuarioApuntado").child(usuarioId.toString()).child(num_dni).setValue(usuarioApuntado)

                        Log.v("[Apuntarse]", "Usuario Apuntado: $usuarioApuntado")
                        Log.v("[Apuntarse]", "Usuario: $basedatosRef")

                    }

                    override fun onCancelled(error: DatabaseError) {
                        println("Error al obtener datos: ")
                    }
                })

               Toast.makeText(requireContext(), "Apuntado correctamente", Toast.LENGTH_SHORT).show()

                requireActivity().onBackPressed()


            }else{
                Toast.makeText(requireContext(), "Formato del dni incorrecto", Toast.LENGTH_SHORT).show()
            }
        }

        // Si el usuario pulsa el boton de cancelar, vuelve al curso sin crear el usuario
        boton_cancelar.setOnClickListener {
            // LLeva al usuario a la pagina anterior, imitando el boton de atras
            requireActivity().onBackPressed()
        }

        binding.contactanos.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.action_apuntarse_curso_to_nav_contactenos)
        }

        // TODO RECUERDA MANEJAR ERROR DE LOS EDITTEXT, Y METEMOS TEXTO EN ROJO SI VEMOS ALGO MAL, LO DESTRUIMOS EN EL BOTON REGISTRASE
    }

    fun comprobarDNI(dni: String): Boolean {
        if((dni.length - 1) == 8 ){
            val ultimaLetra = dni.last().uppercaseChar()
            if (ultimaLetra.isLetter() && ultimaLetra in 'A'..'Z'){
                return true
            }
        }

        return false
    }

}