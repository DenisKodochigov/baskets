package com.example.shopping_list.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shopping_list.ui.AppViewModel
import com.example.shopping_list.ui.baskets.BasketsScreen
import com.example.shopping_list.ui.products.ProductsScreen
import com.example.shopping_list.ui.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    bottomSheetContent: MutableState <@Composable (() -> Unit)?>,
) {
    val viewModel = hiltViewModel<AppViewModel>()
    NavHost(navController = navController, startDestination = Baskets.route, modifier = modifier){

        composable( route = Baskets.route) {
            BasketsScreen(
                onBasketClick = { navController.navigateToProducts(it) },
                viewModel = viewModel,
                bottomSheetContent = bottomSheetContent)
        }

        composable( route = ProductsBasket.routeWithArgs, arguments = ProductsBasket.arguments )
        { navBackStackEntry ->
            val basketId = navBackStackEntry.arguments?.getInt(ProductsBasket.basketIdArg)
            if (basketId != null) {
                ProductsScreen(
                    basketId = basketId,
                    viewModel = viewModel,
                    bottomSheetContent = bottomSheetContent
                )
            }
        }

        composable( route = Products.route) {
            ProductsScreen(
                basketId = -1,
                viewModel = viewModel,
                bottomSheetContent = bottomSheetContent)
        }
        composable( route = Setting.route) {
            SettingsScreen(
                onSettingsClick = { navController.navigateToScreen(it) },
                viewModel = viewModel,
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

