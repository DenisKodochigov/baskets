package com.example.basket.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.basket.entity.SizeElement
import com.example.basket.navigation.AppNavHost
import com.example.basket.navigation.BasketsDestination
import com.example.basket.navigation.SettingDestination
import com.example.basket.navigation.appTabRowScreens
import com.example.basket.navigation.listScreens
import com.example.basket.navigation.navigateToScreen
import com.example.basket.ui.components.AppBottomBar
import com.example.basket.ui.components.ExtendedFAB
import com.example.basket.ui.theme.AppTheme
import com.example.basket.ui.theme.sizeApp
import com.example.basket.utils.bottomBarAnimatedScroll
import com.example.basket.utils.log
import kotlin.math.roundToInt

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("RememberReturnType", "UnrememberedMutableState", "SuspiciousIndentation")
@Composable
fun MainApp() {
    AppTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val offsetHeightPx = remember { mutableFloatStateOf(0f) }
        val refreshScreen = remember { mutableStateOf(true) }

        Scaffold(
            modifier = Modifier
                .padding(14.dp)
                .semantics { testTagsAsResourceId = true }
                .background(color = MaterialTheme.colorScheme.background)
                .bottomBarAnimatedScroll( offsetHeightPx = offsetHeightPx,
                    height = sizeApp(SizeElement.HEIGHT_BOTTOM_BAR)),
            bottomBar = {
                offsetHeightPx.floatValue = 0f
                AppBottomBar(
                    currentScreen = appTabRowScreens.find {
                        it.route == currentDestination?.route?.substringBefore("/") } ?: BasketsDestination,
                    modifier = Modifier
                        .height(sizeApp(SizeElement.HEIGHT_BOTTOM_BAR))
                        .offset { IntOffset(x = 0, y = -offsetHeightPx.floatValue.roundToInt())},
                    onTabSelection = { newScreen -> navController.navigateToScreen(newScreen.route)})
            },
            floatingActionButton = {
                val currentScreen = listScreens.find { it.route ==
                        currentDestination?.route?.substringBefore("/") } ?: BasketsDestination
                if (currentScreen != SettingDestination ) {
                    ExtendedFAB(
                        text =  currentScreen.textFAB,
                        onClick = currentScreen.onClickFAB,
                        modifier = Modifier
                            .offset { IntOffset(x = 0, y = -offsetHeightPx.floatValue.roundToInt()) }
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End,
        ) { innerPadding ->
            val plug = innerPadding
            AppNavHost(
                navController = navController,
                modifier = Modifier, //.padding(innerPadding),
                refreshScreen = refreshScreen)
        }
    }
}

@Preview
@Composable fun MainAppPreview(){
    MainApp()
}