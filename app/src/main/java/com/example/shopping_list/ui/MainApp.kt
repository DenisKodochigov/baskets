package com.example.shopping_list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shopping_list.navigation.AppNavHost
import com.example.shopping_list.navigation.Overview
import com.example.shopping_list.navigation.SingleAccount
import com.example.shopping_list.navigation.appTabRowScreens
import com.example.shopping_list.ui.components.BottomTabRow
import com.example.shopping_list.ui.theme.BorderBottomBar
import com.example.shopping_list.ui.theme.BottomBackgroundColor
import com.example.shopping_list.ui.theme.BottomSheetTheme

@Composable
fun MainApp() {
    BottomSheetTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = appTabRowScreens.find { it.route == currentDestination?.route } ?: Overview
        val cornerRadius = 20.dp

        Scaffold(
            bottomBar = {
                BottomAppBar(
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    modifier = Modifier
                        .border(
                            1.dp,
                            color = BorderBottomBar,
                            shape = RoundedCornerShape(cornerRadius)
                        )
                        .background(
                            color = BottomBackgroundColor,
                            shape = RoundedCornerShape(cornerRadius)
                        ),
//                    cutoutShape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
                ){
                    BottomTabRow(
                        allScreens = appTabRowScreens,
                        currentScreen = currentScreen,
                        onTabSelected = { newScreen ->
                            navController.navigateSingleTopTo(newScreen.route) })
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* ... */ },
                    modifier = Modifier.border(2.dp, color = BorderBottomBar, shape = RoundedCornerShape(50.dp))    ,
                    backgroundColor = BottomBackgroundColor,
                    contentColor = BorderBottomBar
                ) {
                    Icon(Icons.Filled.Add,"")
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true,
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding))
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
    }
private fun NavHostController.navigateToSingleAccount(accountType: String) {
    this.navigateSingleTopTo("${SingleAccount.route}/$accountType")
}
@Preview
@Composable
fun MainAppPreview(){
    MainApp()
}
