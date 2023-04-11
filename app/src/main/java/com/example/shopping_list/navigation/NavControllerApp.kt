package com.example.shopping_list.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
    }
fun NavHostController.navigateToSingleAccount(accountType: String) {
    this.navigateSingleTopTo("${SingleAccount.route}/$accountType")
}

fun NavHostController.navigateProductsBasketTo(route: String) =
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(this@navigateProductsBasketTo.graph.findStartDestination().id) {
            saveState = true
        }
    }
fun NavHostController.navigateToProductsBasket(basketId: String) {
    this.navigateProductsBasketTo("${ProductsBasket.route}/$basketId")
}