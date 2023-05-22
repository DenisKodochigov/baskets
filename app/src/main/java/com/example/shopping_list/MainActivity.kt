package com.example.shopping_list

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.shopping_list.ui.AppViewModel
import com.example.shopping_list.ui.MainApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: AppViewModel by viewModels()
        viewModel.getListBasket()
        setContent {
            MainApp(viewModel)
        }
    }
}
