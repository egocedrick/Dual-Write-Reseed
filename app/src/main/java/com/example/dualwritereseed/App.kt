package com.example.dualwritereseed

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DualWriteManager.reseedIfMissing(this)
    }
}