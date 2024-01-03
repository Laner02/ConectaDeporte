package com.laner.conectadeporte

import android.app.Application

class UserApp : Application() {

    companion object{
        lateinit var prefs: Prefs
    }
    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
    }


}