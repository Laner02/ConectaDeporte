package com.laner.conectadeporte

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Prefs(val context: Context) {

    val NOMBRE_DB = "ConectaDeporte"
    val SHARED_USER_EMAIL = "useremail"
    val SHARED_ID = "id_user"

    val storage = context.getSharedPreferences(NOMBRE_DB, 0)

    var db = Firebase.firestore

    fun saveEmail(email: String){
        storage.edit().putString(SHARED_USER_EMAIL, email).apply()
    }

    fun getEmail(): String{
        return storage.getString(SHARED_USER_EMAIL, "")!!
    }

    fun wipe(){
        storage.edit().clear().apply()

    }

    fun getUserId() {
        db.collection("Usuario").whereEqualTo("email", UserApp.prefs.getEmail()).get()
            .addOnSuccessListener {
                /*for(documentos in it){
                Log.d("Documento query:", "${documentos.data}")
            }
        }*/

            }
    }

}