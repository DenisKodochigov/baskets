package com.example.basket.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Sailing
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
    var textFAB: String
    val pictureDay: Int
    val pictureNight: Int
    val textHeader: Int
    var onClickFAB: () -> Unit
}

/*** App app navigation destinations*/
object BasketsDestination : ScreenDestination {
    override val icon = Icons.Filled.ShoppingBasket
    override val route = "baskets"
    override var textFAB = ""
    override val pictureDay = R.drawable.basket_d
    override val pictureNight = R.drawable.basket_n
    override val textHeader = R.string.baskets
    override var onClickFAB: () -> Unit = {  }
}

object ProductsDestination : ScreenDestination {
    override val icon = Icons.Filled.Dashboard
    override val route = "products"
    override var textFAB = ""
    const val basketIdArg = "basket_type"
    val routeWithArgs = "${route}/{$basketIdArg}"
    val arguments = listOf(navArgument(basketIdArg) { type = NavType.LongType })
    override val pictureDay = R.drawable.fon1_d
    override val pictureNight = R.drawable.fon1_n
    override val textHeader = R.string.products_in_basket
    override var onClickFAB: () -> Unit = {  }
}

object ArticlesDestination : ScreenDestination {
    override val icon = Icons.Filled.Dashboard
    override val route = "article"
    override var textFAB = ""
    override val pictureDay = R.drawable.fon5_d
    override val pictureNight = R.drawable.fon5_n
    override val textHeader = R.string.products
    override var onClickFAB: () -> Unit = {  }
}

object SettingDestination : ScreenDestination {
    override val icon = Icons.Filled.Settings
    override val route = "settings"
    override var textFAB = ""
    override val pictureDay = 0
    override val pictureNight = 0
    override val textHeader = R.string.settings_page
    override var onClickFAB: () -> Unit = {  }
}
//object TestDestination : ScreenDestination {
//    override val icon = Icons.Filled.Sailing
//    override val route = "test"
//    override var textFAB = ""
//    override val pictureDay = 0
//    override val pictureNight = 0
//    override val textHeader = R.string.settings_page
//    override var onClickFAB: () -> Unit = {  }
//}

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
val appTabRowScreens = listOf(BasketsDestination, ArticlesDestination, SettingDestination) //, TestDestination)
val listScreens = listOf(BasketsDestination, ArticlesDestination, ProductsDestination, SettingDestination)
