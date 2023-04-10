/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.shopping_list.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

/*** Contract for information needed on every App navigation destination*/
interface ScreenDestination {
    val icon: ImageVector
    val route: String
}

/*** App app navigation destinations*/
object Baskets : ScreenDestination {
    override val icon = Icons.Filled.ShoppingBasket
    override val route = "baskets"
}

object ProductsBasket : ScreenDestination {
    override val icon = Icons.Filled.Dashboard
    override val route = "products_basket"
    const val basketIdArg = "basket_type"
    val routeWithArgs = "${route}/{$basketIdArg}"
    val arguments = listOf(navArgument(basketIdArg) { type = NavType.StringType })
}
object Accounts : ScreenDestination {
    override val icon = Icons.Filled.Dashboard
    override val route = "products"
}

object Products : ScreenDestination {
    override val icon = Icons.Filled.Dashboard
    override val route = "prodacts"
}

object Setting : ScreenDestination {
    override val icon = Icons.Filled.Settings
    override val route = "settings"
}

object SingleAccount : ScreenDestination {
    // Added for simplicity, this icon will not in fact be used, as SingleAccount isn't
    // part of the AppTabRow selection
    override val icon = Icons.Filled.Money
    override val route = "single_account"
    const val accountTypeArg = "account_type"
    val routeWithArgs = "$route/{$accountTypeArg}"
    val arguments = listOf(
        navArgument(accountTypeArg) { type = NavType.StringType })
    val deepLinks = listOf(
        navDeepLink { uriPattern = "App://$route/{$accountTypeArg}"})
}

// Screens to be displayed in the top AppTabRow
val appTabRowScreens = listOf(Baskets, Products, Setting)
