package com.example.shopping_list.navigation

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
import com.example.shopping_list.ui.article.ArticlesScreen
import com.example.shopping_list.ui.baskets.BasketsScreen
import com.example.shopping_list.ui.products.ProductsScreen
import com.example.shopping_list.ui.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    showBottomSheet: MutableState<Boolean>
){

    NavHost(
        navController = navController, startDestination = Baskets.route, modifier = modifier ) {
        composable(route = Baskets.route,
            enterTransition = {
                targetState.destination.route?.let { enterTransition(Baskets.route, it) } },
            exitTransition = {
                targetState.destination.route?.let { exitTransition(Baskets.route, it)  } }) {
            BasketsScreen(
                showBottomSheet = showBottomSheet,
                onClickBasket = { navController.navigateToProducts(it) },
            )
        }
        composable(
            route = ProductsBasket.routeWithArgs, arguments = ProductsBasket.arguments,
            enterTransition = {
                targetState.destination.route?.let { enterTransition(Baskets.route, it) } },
            exitTransition = {
                targetState.destination.route?.let { exitTransition(Baskets.route, it)  } }
        )
        { navBackStackEntry ->
            val basketId = navBackStackEntry.arguments?.getLong(ProductsBasket.basketIdArg)
            if (basketId != null) {
                ProductsScreen(basketId = basketId, showBottomSheet = showBottomSheet,)
            }
        }
        composable(
            route = Articles.route,
            enterTransition = {
                targetState.destination.route?.let { enterTransition(Baskets.route, it) } },
            exitTransition = {
                targetState.destination.route?.let { exitTransition(Baskets.route, it)  } }
        ) {
            ArticlesScreen( showBottomSheet = showBottomSheet )
        }
        composable(
            route = Setting.route,
            enterTransition = {
                targetState.destination.route?.let { enterTransition(Baskets.route, it) } },
            exitTransition = {
                targetState.destination.route?.let { exitTransition(Baskets.route, it)  } }
        ) {
            SettingsScreen()
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

