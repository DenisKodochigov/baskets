package com.example.basket.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.basket.ui.screens.article.ArticlesScreen
import com.example.basket.ui.screens.baskets.BasketsScreen
import com.example.basket.ui.screens.products.ProductsScreen
import com.example.basket.ui.screens.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    refreshScreen: MutableState<Boolean>,
){

    NavHost(
        navController = navController, startDestination = BasketsDestination.route, modifier = modifier ) {
        composable(route = BasketsDestination.route,
            enterTransition = {
                targetState.destination.route?.let { enterTransition(BasketsDestination.route, it) } },
            exitTransition = {
                targetState.destination.route?.let { exitTransition(BasketsDestination.route, it)  } }) {
            BasketsScreen(
                screen = BasketsDestination,
                onClickBasket = { navController.navigateToProducts(it) },
            )
        }
        composable(
            route = ProductsDestination.routeWithArgs, arguments = ProductsDestination.arguments,
            enterTransition = {
                targetState.destination.route?.let { enterTransition(BasketsDestination.route, it) } },
            exitTransition = {
                targetState.destination.route?.let { exitTransition(BasketsDestination.route, it)  } }
        )
        { navBackStackEntry ->
            val basketId = navBackStackEntry.arguments?.getLong(ProductsDestination.basketIdArg)
            if (basketId != null) {
                ProductsScreen(basketId = basketId, screen = ProductsDestination)
            }
        }
        composable(
            route = ArticlesDestination.route,
            enterTransition = {
                targetState.destination.route?.let { enterTransition(BasketsDestination.route, it) } },
            exitTransition = {
                targetState.destination.route?.let { exitTransition(BasketsDestination.route, it)  } }
        ) {
            ArticlesScreen( screen = ArticlesDestination )
        }
        composable(
            route = SettingDestination.route,
            enterTransition = {
                targetState.destination.route?.let { enterTransition(BasketsDestination.route, it) } },
            exitTransition = {
                targetState.destination.route?.let { exitTransition(BasketsDestination.route, it)  } }
        ) {
            SettingsScreen(refreshScreen = refreshScreen, screen = SettingDestination)
        }
    }
}

fun enterTransition(defaultScreen: String, targetScreen: String): EnterTransition{
    val durationMillis = 800
    val delayMillis = 200
    return if (targetScreen == defaultScreen) {
        slideInHorizontally(
            animationSpec = tween( durationMillis = durationMillis, delayMillis = delayMillis)) { it / -1 } +
                fadeIn( animationSpec = tween(durationMillis = durationMillis, delayMillis = delayMillis))
    } else {
        slideInHorizontally(
            animationSpec = tween(durationMillis = durationMillis, delayMillis = delayMillis)) { it / 1 } +
                fadeIn( animationSpec = tween(durationMillis = durationMillis, delayMillis = delayMillis))
    }
}
fun exitTransition(defaultScreen: String, targetScreen: String): ExitTransition {
    val durationMillis = 800
    val delayMillis = 200
    return if (targetScreen == defaultScreen) {
        slideOutHorizontally(
            animationSpec = tween(durationMillis = durationMillis, delayMillis = delayMillis)) { it / 1 } +
                fadeOut(animationSpec = tween(durationMillis = durationMillis, delayMillis = delayMillis))
    } else {
        slideOutHorizontally(
            animationSpec = tween(durationMillis = durationMillis, delayMillis = delayMillis)) { it / -1 } +
                fadeOut(animationSpec = tween(durationMillis = durationMillis, delayMillis = delayMillis))
    }
}

