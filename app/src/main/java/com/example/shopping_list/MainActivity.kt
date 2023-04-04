package com.example.shopping_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shopping_list.databinding.ActivityMainBinding
import com.example.shopping_list.navigation.AppNavHost
import com.example.shopping_list.navigation.Overview
import com.example.shopping_list.navigation.SingleAccount
import com.example.shopping_list.navigation.appTabRowScreens
import com.example.shopping_list.ui.MainApp
import com.example.shopping_list.ui.components.BottomTabRow
import com.example.shopping_list.ui.theme.MainTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    // Instance of the AppComponent that will be used by all the Activities in the project

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp()
        }
    }
}
