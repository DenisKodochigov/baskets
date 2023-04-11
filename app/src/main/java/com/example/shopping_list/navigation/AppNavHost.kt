package com.example.shopping_list.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shopping_list.ui.accounts.AccountsScreen
import com.example.shopping_list.ui.accounts.SingleAccountScreen
import com.example.shopping_list.ui.baskets.BasketsScreen
import com.example.shopping_list.ui.products.ProductsScreen
import com.example.shopping_list.ui.products_basket.ProductsBasketScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
//    val viewModel = hiltViewModel<AppViewModel>()
    NavHost(navController = navController, startDestination = Baskets.route, modifier = modifier){

        composable( route = Baskets.route) {
//            BasketsScreen( onBasketClick = { navController.navigateProductsBasketTo(it) }, viewModel)
            BasketsScreen( onBasketClick = { navController.navigateProductsBasketTo(it) })
        }

        composable( route = ProductsBasket.routeWithArgs, arguments = ProductsBasket.arguments )
        { navBackStackEntry ->
            val basketId = navBackStackEntry.arguments?.getString(ProductsBasket.basketIdArg)
            if (basketId != null) {
                ProductsBasketScreen(basketId)
            }
        }

        composable( route = Products.route) {
            ProductsScreen()
        }

        composable(route = Accounts.route) {
            AccountsScreen(
                onAccountClick = { accountType -> navController.navigateToSingleAccount(accountType) })
        }
//        composable(route = Bills.route) { BillsScreen() }
        composable(
            route = SingleAccount.routeWithArgs,
            arguments = SingleAccount.arguments,
            deepLinks = SingleAccount.deepLinks
        ) { navBackStackEntry ->
            val accountType = navBackStackEntry.arguments?.getString(SingleAccount.accountTypeArg)
            SingleAccountScreen(accountType)
        }
    }
}

