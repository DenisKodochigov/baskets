package com.example.shopping_list.entity

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.shopping_list.R
import com.example.shopping_list.utils.log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ErrorApp @Inject constructor(@ApplicationContext val context: Context) {

    fun errorApi (errorMessage:String){

        val toastMessage: String
        log( true,"Error: $errorMessage")

        when(errorMessage){
            "error_addProduct" -> toastMessage = context.getString(R.string.error_addProduct)
            else -> toastMessage = context.getString(R.string.errorUser)
        }
        if (toastMessage.isNotEmpty()) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

