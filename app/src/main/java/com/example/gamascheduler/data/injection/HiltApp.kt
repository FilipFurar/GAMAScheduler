package com.example.gamascheduler.data.injection

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GAMASchedulerHiltApp: Application() {
    override fun onCreate() {
        super.onCreate()
        println("Initializing Firebase manually...") //lebo predbieha HILD :((
        com.google.firebase.FirebaseApp.initializeApp(this)
    }
}