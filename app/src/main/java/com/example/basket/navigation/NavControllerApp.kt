package com.example.basket.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.basket.utils.log

fun NavHostController.navigateToScreen(route: String) =
    this.navigate(route) {
        launchSingleTop = true
        popUpTo(this@navigateToScreen.graph.findStartDestination().id) {
            saveState = true
        }
    }

fun NavHostController.navigateToProducts(basketId: Long) {
    this.navigateToScreen("${ProductsDestination.route}/$basketId")
}