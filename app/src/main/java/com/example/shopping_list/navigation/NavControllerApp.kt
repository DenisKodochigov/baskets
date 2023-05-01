package com.example.shopping_list.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

fun NavHostController.navigateToScreen(route: String) =
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(this@navigateToScreen.graph.findStartDestination().id) {
            saveState = true
        }
    }

fun NavHostController.navigateToProducts(basketId: Long) {
    this.navigateToScreen("${ProductsBasket.route}/$basketId")
}