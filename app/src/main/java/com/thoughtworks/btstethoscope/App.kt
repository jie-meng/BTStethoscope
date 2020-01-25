package com.thoughtworks.btstethoscope

import androidx.multidex.MultiDexApplication
import com.thoughtworks.btstethoscope.injection.AppComponent
import com.thoughtworks.btstethoscope.injection.AppModule
import com.thoughtworks.btstethoscope.injection.DaggerAppComponent

class App : MultiDexApplication() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}