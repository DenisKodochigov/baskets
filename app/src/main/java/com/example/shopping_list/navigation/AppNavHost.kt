package com.example.shopping_list.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.shopping_list.ui.article.ArticlesScreen
import com.example.shopping_list.ui.baskets.BasketsScreen
import com.example.shopping_list.ui.products.ProductsScreen
import com.example.shopping_list.ui.settings.SettingsScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppAnimatedNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    bottomSheetVisible: MutableState<Boolean>,
    bottomSheetContent: MutableState <@Composable (() -> Unit)?>,
    bottomSheetHide: () -> Unit
){

    AnimatedNavHost(
        navController = navController, startDestination = Baskets.route, modifier = modifier ) {
        val durationMillis = 1500
        val delayMillis = 200
        val enterTransition = slideInHorizontally(
            animationSpec = tween(durationMillis = durationMillis, delayMillis = delayMillis)) { it / 1 } +
                fadeIn( animationSpec = tween(durationMillis = durationMillis, delayMillis = delayMillis))
        val exitTransition = slideOutHorizontally(
            animationSpec = tween(durationMillis = durationMillis, delayMillis = delayMillis)) { it / -1 } +
                fadeOut(animationSpec = tween(durationMillis = durationMillis, delayMillis = delayMillis))
        
        composable(route = Baskets.route,
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },){
            BasketsScreen(
                bottomSheetContent = bottomSheetContent,
                bottomSheetHide = bottomSheetHide,
                bottomSheetVisible = bottomSheetVisible,
                onClickBasket = { navController.navigateToProducts(it) },)
        }
        composable(route = ProductsBasket.routeWithArgs, arguments = ProductsBasket.arguments,
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },)
        { navBackStackEntry ->
            val basketId = navBackStackEntry.arguments?.getLong(ProductsBasket.basketIdArg)
            if (basketId != null) {
                ProductsScreen(
                    basketId = basketId,
                    bottomSheetVisible = bottomSheetVisible,
                    bottomSheetContent = bottomSheetContent
                )
            }
        }
        composable(route = Articles.route,
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },){
            ArticlesScreen(
                bottomSheetContent = bottomSheetContent,
                bottomSheetVisible = bottomSheetVisible,)
        }
        composable(route = Setting.route,
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },){
            SettingsScreen()
        }

    }
}

//

//@ExperimentalAnimationApi
//@Composable
//fun AnimationScreen() {
//    val navController = rememberAnimatedNavController()
//val slideEffect = spring<IntOffset>(dampingRatio = Spring.DampingRatioMediumBouncy)
//val popupEffect = tween<IntOffset>(durationMillis = 2000, easing = CubicBezierEasing(0.08f,0.93f,0.68f,1.27f))
//
//    AnimatedNavHost(navController, startDestination = "main") {
//        composable(
//            Baskets.route,
//            enterTransition = {
//                slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = slideEffect) },
//            exitTransition = {
//                slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = slideEffect) },
//            popEnterTransition = {
//                slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = slideEffect) },
//            popExitTransition = {
//                slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = slideEffect) }
//        ) { }
//
//
//        composable(
//            Articles.route,
//            enterTransition = {
//                slideInVertically(initialOffsetY = { 1000 }, animationSpec = popupEffect) },
//            exitTransition = {
//                slideOutVertically(targetOffsetY = { -1000 }, animationSpec = popupEffect) },
//            popEnterTransition = {
//                slideInVertically(initialOffsetY = { -1000 }, animationSpec = popupEffect) },
//            popExitTransition = {
//                slideOutVertically(targetOffsetY = { 1000 }, animationSpec = popupEffect) }
//        ) {}
//        composable("main") {
//
//        }
//    }
//}

