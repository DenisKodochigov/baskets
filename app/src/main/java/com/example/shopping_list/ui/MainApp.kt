package com.example.shopping_list.ui

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shopping_list.navigation.*
import com.example.shopping_list.ui.components.BottomBarApp
import com.example.shopping_list.ui.components.FloatingActionButtonApp
import com.example.shopping_list.ui.theme.AppTheme
import com.example.shopping_list.ui.theme.BackgroundBottomSheet
import kotlinx.coroutines.launch

@SuppressLint("RememberReturnType", "UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainApp() {
    AppTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = appTabRowScreens.find { it.route == currentDestination?.route } ?: Baskets
        val bottomSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            animationSpec = tween(durationMillis = 500, delayMillis = 0, easing = FastOutLinearInEasing))
        val bottomSheetContent = remember { mutableStateOf<@Composable (() -> Unit)?>({ })  }
        val scope  = rememberCoroutineScope()

        ModalBottomSheetLayout(
            sheetState  = bottomSheetState,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetBackgroundColor = BackgroundBottomSheet,
            sheetContent = { bottomSheetContent.value?.invoke() },
        ) {
            Scaffold(
                modifier = Modifier.background(MaterialTheme.colors.background).padding(12.dp),
                bottomBar = {
                    BottomBarApp(
                        currentScreen = currentScreen,
                        onTabSelection = { newScreen ->
                            navController.navigateToScreen(newScreen.route) }) },
                floatingActionButton = {
                    FloatingActionButtonApp( onClick = { scope.launch {
                        if (bottomSheetContent.value != null) { bottomSheetState.show() }}})
                },
                floatingActionButtonPosition = FabPosition.Center,
                isFloatingActionButtonDocked = true,
            ) { innerPadding ->
                AppNavHost(navController = navController,
                    modifier = Modifier.padding(innerPadding),
                    bottomSheetContent = bottomSheetContent,
                    bottomSheetVisible = mutableStateOf( bottomSheetState.currentValue != ModalBottomSheetValue.Hidden),
                    bottomSheetHide = { scope.launch {bottomSheetState.hide() }})
            }
        }
    }
}

