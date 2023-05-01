package com.example.shopping_list

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application()  {

    override fun onCreate() {
        super.onCreate()
//        context = this
    }
    companion object{
//        lateinit var context: Context
//            private set
//        var basket: BasketEntity? = null
    }
}