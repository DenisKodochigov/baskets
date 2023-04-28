package com.example.shopping_list.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shopping_list.navigation.*
import com.example.shopping_list.ui.components.BottomBarApp
import com.example.shopping_list.ui.components.FloatingActionButtonApp
import com.example.shopping_list.ui.theme.AppTheme
import com.example.shopping_list.ui.theme.BackgroundBottomSheet
import kotlinx.coroutines.launch

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainApp(viewModel: AppViewModel) {
    AppTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = appTabRowScreens.find { it.route == currentDestination?.route } ?: Baskets
        val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val bottomSheetContent = remember { mutableStateOf<@Composable (() -> Unit)?>({ })  }
        val scope  = rememberCoroutineScope()

        ModalBottomSheetLayout(
            sheetState  = bottomSheetState,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetBackgroundColor = BackgroundBottomSheet,
            sheetContent = { bottomSheetContent.value?.invoke() },
        ) {
            Scaffold(
                bottomBar = {
                    BottomBarApp( currentScreen = currentScreen, onTabSelection = { newScreen ->
                        navController.navigateToScreen(newScreen.route) }) },
                floatingActionButton = {
                    FloatingActionButtonApp( onClick = { scope.launch {
                        if (bottomSheetContent.value != null) {
//                            Log.d("KDS", "bottomSheetContent.value = ${bottomSheetContent.value} ")
                            bottomSheetState.show() }}})},
                floatingActionButtonPosition = FabPosition.Center,
                isFloatingActionButtonDocked = true,
            ) { innerPadding ->
                AppNavHost(navController, modifier = Modifier.padding(innerPadding),
                    viewModel = viewModel, bottomSheetContent = bottomSheetContent)
            }
        }
    }
}

