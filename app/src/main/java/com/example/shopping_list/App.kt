package com.example.shopping_list

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application()  {
    companion object AppObj{
        var scale: Int = 1
    }
}