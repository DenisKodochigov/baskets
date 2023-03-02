package com.example.shopping_list.entity

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.shopping_list.App
import com.example.shopping_list.R
import com.example.shopping_list.data.DataRepository

class ErrorApp {

    fun errorApi (errorMessage:String){
        val toastMessage: String
        when(errorMessage){
            "HTTP 401 " -> {
                toastMessage = App.context.getString(R.string.error401)
            }
            "HTTP 402 " -> {
                toastMessage = App.context.getString(R.string.error402)
                setNewKeyApi()
            }
            "HTTP 404 " -> {
                toastMessage = App.context.getString(R.string.error404)
            }
            "HTTP 429 " -> {
                toastMessage = App.context.getString(R.string.error429)
            }
            else -> {
                toastMessage = App.context.getString(R.string.errorUser)
                Log.d("KDS", "Error $errorMessage")
            }

        }
        if (toastMessage.isNotEmpty()) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(App.context, toastMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setNewKeyApi(){
        val dataRepository = DataRepository()
//        dataRepository.setApiKey()
    }
}

