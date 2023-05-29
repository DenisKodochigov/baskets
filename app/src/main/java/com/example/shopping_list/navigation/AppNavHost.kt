package com.example.shopping_list.navigation

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
    bottomSheetContent: MutableState <@Composable (() -> Unit)?>,
    bottomSheetHide: () -> Unit,
) {

    NavHost(navController = navController, startDestination = Baskets.route, modifier = modifier){

        composable( route = Baskets.route) {
            BasketsScreen(
                bottomSheetContent = bottomSheetContent,
                bottomSheetHide = bottomSheetHide,
                onClickBasket = { navController.navigateToProducts(it) },)
        }

        composable( route = ProductsBasket.routeWithArgs, arguments = ProductsBasket.arguments )
        { navBackStackEntry ->
            val basketId = navBackStackEntry.arguments?.getLong(ProductsBasket.basketIdArg)
            if (basketId != null) {
                ProductsScreen(
                    basketId = basketId,
                    bottomSheetContent = bottomSheetContent,
                    bottomSheetHide = bottomSheetHide
                )
            }
        }

        composable( route = Articles.route) {
            ArticlesScreen(
                bottomSheetContent = bottomSheetContent,
                bottomSheetHide = bottomSheetHide)
        }
        composable( route = Setting.route) {
            SettingsScreen(
                onSettingsClick = { navController.navigateToScreen(it) },
                bottomSheetContent = bottomSheetContent)
        }
//        composable(
//            route = SingleAccount.routeWithArgs,
//            arguments = SingleAccount.arguments,
//            deepLinks = SingleAccount.deepLinks
//        ) { navBackStackEntry ->
//            val accountType = navBackStackEntry.arguments?.getString(SingleAccount.accountTypeArg)
//            SingleAccountScreen(accountType)
//        }
    }
}

