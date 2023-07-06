package com.example.shopping_list.ui

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.shopping_list.ui.theme.BackgroundBottomSheet
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.launch

@SuppressLint("RememberReturnType", "UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class,)
@Composable
fun MainApp() {
    AppTheme {
        val bottomSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            animationSpec = tween(durationMillis = 500, delayMillis = 0, easing = FastOutLinearInEasing))
        val bottomSheetContent = remember { mutableStateOf<@Composable (() -> Unit)?>({ })  }
        val scope  = rememberCoroutineScope()

        val animatedNavController  = rememberAnimatedNavController()
        val animCurrentBackStack by animatedNavController.currentBackStackEntryAsState()
        val animCurrentDestination = animCurrentBackStack?.destination
        val animCurrentScreen = appTabRowScreens.find { it.route == animCurrentDestination?.route } ?: Baskets

        ModalBottomSheetLayout(
            sheetState  = bottomSheetState,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetBackgroundColor = BackgroundBottomSheet,
            sheetContent = { bottomSheetContent.value?.invoke() },
        ) {
            Scaffold(
                modifier = Modifier.background(MaterialTheme.colors.background).padding(14.dp),
                bottomBar = {
                    BottomBarApp(
                        currentScreen = animCurrentScreen, //currentScreen,
                        onTabSelection = { newScreen ->
                            animatedNavController.navigateToScreen(newScreen.route) }) },
                floatingActionButton = {
                    FloatingActionButtonApp(offset = 0.dp, top = 70.dp, icon = Icons.Filled.Add,
                        onClick = { scope.launch {
                        if (bottomSheetContent.value != null) { bottomSheetState.show() }}})
                },
                floatingActionButtonPosition = FabPosition.Center,
                isFloatingActionButtonDocked = true,
            ) { innerPadding ->
                AppAnimatedNavHost( navController = animatedNavController,
                    modifier = Modifier.padding(innerPadding),
                    bottomSheetContent = bottomSheetContent,
                    bottomSheetHide = { scope.launch {bottomSheetState.hide() }})
            }
        }
    }
}

