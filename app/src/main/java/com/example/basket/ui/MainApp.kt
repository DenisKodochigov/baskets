package com.example.basket.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.basket.entity.SizeElement
import com.example.basket.navigation.AppNavHost
import com.example.basket.navigation.Baskets
import com.example.basket.navigation.appTabRowScreens
import com.example.basket.navigation.navigateToScreen
import com.example.basket.ui.components.BottomBarApp
import com.example.basket.ui.components.FloatingActionButtonApp
import com.example.basket.ui.theme.AppTheme
import com.example.basket.ui.theme.sizeApp
import kotlin.math.roundToInt

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("RememberReturnType", "UnrememberedMutableState", "SuspiciousIndentation")
@Composable
fun MainApp() {
    AppTheme {
        val showBottomSheet = remember { mutableStateOf(false) }
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val animCurrentScreen = appTabRowScreens.find {
            it.route == currentDestination?.route
        } ?: Baskets
        val bottomBarHeight = sizeApp(SizeElement.SIZE_ICON) + 12.dp
        val bottomBarOffsetHeightPx = remember { mutableStateOf(0f) }
        val refreshScreen = remember { mutableStateOf(true) }

        Scaffold(
            modifier = Modifier
                .padding(14.dp)
                .semantics { testTagsAsResourceId = true }
                .background(color = MaterialTheme.colorScheme.background)
                .bottomBarAnimatedScroll(
                     height = bottomBarHeight, offsetHeightPx = bottomBarOffsetHeightPx),
            bottomBar = {
                BottomBarApp(
                    currentScreen = animCurrentScreen, //currentScreen,
                    modifier = Modifier
                        .height(bottomBarHeight)
                        .offset {
                            IntOffset(x = 0, y = -bottomBarOffsetHeightPx.value.roundToInt()) },
                    onTabSelection = { newScreen ->
                        navController.navigateToScreen(newScreen.route)
                    })
            },
            floatingActionButton = {
                FloatingActionButtonApp(
                    refreshScreen = refreshScreen,
                    modifier = Modifier
                        .offset {
                            IntOffset(x = 0, y = -bottomBarOffsetHeightPx.value.roundToInt()) },
                    offset = sizeApp(SizeElement.OFFSET_FAB), icon = Icons.Filled.Add,
                    onClick = { showBottomSheet.value = true } )
            },
            floatingActionButtonPosition = FabPosition.Center,
        ) { innerPadding ->
            val plug = innerPadding
            AppNavHost(
                navController = navController,
                modifier = Modifier,
                refreshScreen = refreshScreen,
                showBottomSheet = showBottomSheet)
        }
    }
}
fun Modifier.bottomBarAnimatedScroll(height: Dp = 56.dp, offsetHeightPx: MutableState<Float>): Modifier =
    composed {
        val bottomBarHeightPx = with(LocalDensity.current) { height.roundToPx().toFloat() }
        val connection = remember {
            object: NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    val newOffset = offsetHeightPx.value + available.y
                    offsetHeightPx.value = newOffset.coerceIn(-bottomBarHeightPx, 0f)
                    return Offset.Zero
                }
            }
        }
        this.nestedScroll(connection)
    }
@Preview
@Composable fun MainAppPreview(){
    MainApp()
}