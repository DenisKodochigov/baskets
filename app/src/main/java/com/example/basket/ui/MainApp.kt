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
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.basket.ui.components.AppBottomBar
import com.example.basket.ui.components.FloatingActionButtonApp
import com.example.basket.ui.theme.AppTheme
import com.example.basket.ui.theme.sizeApp
import com.example.basket.utils.log
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
            it.route == currentDestination?.route } ?: Baskets
        val bottomBarOffsetHeightPx = remember { mutableFloatStateOf(0f) }
        val refreshScreen = remember { mutableStateOf(true) }

        Scaffold(
            modifier = Modifier
                .padding(14.dp)
                .semantics { testTagsAsResourceId = true }
                .background(color = MaterialTheme.colorScheme.background)
                .bottomBarAnimatedScroll(
                    height = sizeApp(SizeElement.HEIGHT_BOTTOM_BAR),
                    offsetHeightPx = bottomBarOffsetHeightPx),
            bottomBar = {
                AppBottomBar(
                    currentScreen = animCurrentScreen, //currentScreen,
                    modifier = Modifier
                        .height(sizeApp(SizeElement.HEIGHT_BOTTOM_BAR))
                        .offset { IntOffset(x = 0, y = -bottomBarOffsetHeightPx.value.roundToInt()) },
                    onTabSelection = { newScreen -> navController.navigateToScreen(newScreen.route) })
            },
            floatingActionButton = {
                val plug = refreshScreen.value
                FloatingActionButtonApp(
                    icon = Icons.Filled.Add,
                    refreshScreen = refreshScreen,
                    offset = sizeApp(SizeElement.OFFSET_FAB),
                    modifier = Modifier
                        .offset { IntOffset(x = 0, y = -bottomBarOffsetHeightPx.value.roundToInt()) },
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
        val bottomBarHeightPx = with(LocalDensity.current) { (height + 30.dp).roundToPx().toFloat() }
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