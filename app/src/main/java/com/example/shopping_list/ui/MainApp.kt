package com.example.shopping_list.ui

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.shopping_list.navigation.AppAnimatedNavHost
import com.example.shopping_list.navigation.Baskets
import com.example.shopping_list.navigation.appTabRowScreens
import com.example.shopping_list.navigation.navigateToScreen
import com.example.shopping_list.ui.components.BottomBarApp
import com.example.shopping_list.ui.components.FloatingActionButtonApp
import com.example.shopping_list.ui.theme.AppTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@SuppressLint("RememberReturnType", "UnrememberedMutableState", "SuspiciousIndentation")
@OptIn( ExperimentalAnimationApi::class)
@Composable
fun MainApp() {
    AppTheme {

        val showBottomSheet = remember{ mutableStateOf(false)}
        val animatedNavController  = rememberAnimatedNavController()
        val animCurrentBackStack by animatedNavController.currentBackStackEntryAsState()
        val animCurrentDestination = animCurrentBackStack?.destination
        val animCurrentScreen = appTabRowScreens.find { it.route == animCurrentDestination?.route } ?: Baskets

        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.background).padding(14.dp),
            bottomBar = {
                BottomBarApp(
                    currentScreen = animCurrentScreen, //currentScreen,
                    onTabSelection = { newScreen ->
                        animatedNavController.navigateToScreen(newScreen.route) }) },
            floatingActionButton = {
                FloatingActionButtonApp(
                    offset = 80.dp, top = 0.dp, icon = Icons.Filled.Add,
                    onClick = { showBottomSheet.value = true } )
            },
            floatingActionButtonPosition = FabPosition.Center,
        ) { innerPadding ->
            AppAnimatedNavHost(
                navController = animatedNavController,
                modifier = Modifier.padding(innerPadding),
                showBottomSheet = showBottomSheet)
        }
    }
}

