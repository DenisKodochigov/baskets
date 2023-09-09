package com.example.basket.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.basket.navigation.AppNavHost
import com.example.basket.navigation.Baskets
import com.example.basket.navigation.appTabRowScreens
import com.example.basket.navigation.navigateToScreen
import com.example.basket.ui.components.BottomBarApp
import com.example.basket.ui.components.FloatingActionButtonApp
import com.example.basket.ui.theme.AppTheme

@SuppressLint("RememberReturnType", "UnrememberedMutableState", "SuspiciousIndentation")
@Composable
fun MainApp() {
    AppTheme {
        val showBottomSheet = remember{ mutableStateOf(false)}
        val animatedNavController  = rememberNavController()
        val animCurrentBackStack by animatedNavController.currentBackStackEntryAsState()
        val animCurrentDestination = animCurrentBackStack?.destination
        val animCurrentScreen = appTabRowScreens.find {
            it.route == animCurrentDestination?.route } ?: Baskets

        Scaffold(
            modifier = Modifier.padding(14.dp).background(color = MaterialTheme.colorScheme.background),
            bottomBar = {
                BottomBarApp(
                    currentScreen = animCurrentScreen, //currentScreen,
                    onTabSelection = { newScreen ->
                        animatedNavController.navigateToScreen(newScreen.route) }) },
            floatingActionButton = {
                FloatingActionButtonApp(
                    offset = 60.dp, top = 0.dp, icon = Icons.Filled.Add,
                    onClick = { showBottomSheet.value = true } )
            },
            floatingActionButtonPosition = FabPosition.Center,
        ) { innerPadding ->
            AppNavHost(
                navController = animatedNavController,
                modifier = Modifier.padding(innerPadding),
                showBottomSheet = showBottomSheet)
        }
    }
}

