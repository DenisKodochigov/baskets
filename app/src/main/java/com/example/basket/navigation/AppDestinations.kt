package com.example.basket.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.basket.R

/*** Contract for information needed on every App navigation destination*/
interface ScreenDestination {
    val icon: ImageVector
    val route: String
    val pictureDay: Int
    val pictureNight: Int
    val textHeader: Int
}

/*** App app navigation destinations*/
object Baskets : ScreenDestination {
    override val icon = Icons.Filled.ShoppingBasket
    override val route = "baskets"
    override val pictureDay = R.drawable.bas
    override val pictureNight = R.drawable.bas
    override val textHeader = R.string.baskets
}

object ProductsBasket : ScreenDestination {
    override val icon = Icons.Filled.Dashboard
    override val route = "products"
    const val basketIdArg = "basket_type"
    val routeWithArgs = "${route}/{$basketIdArg}"
    val arguments = listOf(navArgument(basketIdArg) { type = NavType.LongType })
    override val pictureDay = R.drawable.fon1
    override val pictureNight = R.drawable.fon1_1
    override val textHeader = R.string.products_in_basket
}

object Articles : ScreenDestination {
    override val icon = Icons.Filled.Dashboard
    override val route = "article"
    override val pictureDay = R.drawable.fon5
    override val pictureNight = R.drawable.fon5_1
    override val textHeader = R.string.product
}

object Setting : ScreenDestination {
    override val icon = Icons.Filled.Settings
    override val route = "settings"
    override val pictureDay = 0
    override val pictureNight = 0
    override val textHeader = R.string.settings_page
}

//object SingleAccount : ScreenDestination {
//    // Added for simplicity, this icon will not in fact be used, as SingleAccount isn't
//    // part of the AppTabRow selection
//    override val icon = Icons.Filled.Money
//    override val route = "single_account"
//    const val accountTypeArg = "account_type"
//    val routeWithArgs = "$route/{$accountTypeArg}"
//    val arguments = listOf(
//        navArgument(accountTypeArg) { type = NavType.StringType })
//    val deepLinks = listOf(
//        navDeepLink { uriPattern = "App://$route/{$accountTypeArg}"})
//}
//object Accounts : ScreenDestination {
//    override val icon = Icons.Filled.Dashboard
//    override val route = "products"
//}
// Screens to be displayed in the top AppTabRow
val appTabRowScreens = listOf(Baskets, Articles, Setting)
