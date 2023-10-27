package com.example.basket

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppBase: Application()  {
    companion object App{
        var scale: Int = 0

    }
}