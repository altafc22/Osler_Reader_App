package com.altafrazzque.oslerreaderapp

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import com.altafrazzque.oslerreaderapp.utilities.Constants
import java.io.File

class MainApp : Application() {



    companion object {
        private lateinit var instance : MainApp
        fun getContext() : Context{
           return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun attachBaseContext(base: Context) {
        val configuration = base.resources.configuration

        if (configuration.fontScale != 1.0f) {
            configuration.fontScale = 1.0f
            super.attachBaseContext(base.createConfigurationContext(configuration))
            return
        }

        super.attachBaseContext(base)
    }

    override fun getResources(): Resources {
        val resources = super.getResources()

        if (resources?.configuration?.fontScale != 1.0f) {
            val configuration = resources.configuration
            configuration.fontScale = 1.0f

            val context = createConfigurationContext(configuration)
            return context.resources
        }

        return resources
    }

    fun createAppDir(){
        val dir = Constants.appDir
        val folder =  File(dir)
        var success = true
        if (!folder.exists()) {
            success = folder.mkdirs()
            if(success)
                System.out.println("Created..$dir")
            else
                System.out.println("Not Created..$dir")
        }
        if (success) {
            // Do something on success
        } else {
            // Do something else on failure
        }
    }
}