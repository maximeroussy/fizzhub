package com.maximeroussy.fizzhub

import android.app.Application
import com.maximeroussy.fizzhub.dependencyinjection.ApplicationComponent
import com.maximeroussy.fizzhub.dependencyinjection.ApplicationModule
import com.maximeroussy.fizzhub.dependencyinjection.DaggerApplicationComponent

class MainApplication : Application() {
  lateinit var component: ApplicationComponent

  override fun onCreate() {
    super.onCreate()
    component = DaggerApplicationComponent.builder()
        .applicationModule(ApplicationModule(this))
        .build()
  }

  companion object {
    private var INSTANCE: MainApplication? = null
    @JvmStatic
    fun get(): MainApplication = INSTANCE!!
  }
}
