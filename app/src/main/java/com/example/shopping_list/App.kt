package com.example.shopping_list

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.shopping_list.data.room.tables.BasketDB
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application()  {

    override fun onCreate() {
        super.onCreate()
//        context = this
    }
    companion object{
        @SuppressLint("StaticFieldLeak")
//        lateinit var context: Context
//            private set
        var basket: BasketDB? = null
    }
}