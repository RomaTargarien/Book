package com.example.criminalintent

import android.app.Application

class CrimeIntentApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.inititialize(this)
    }
}