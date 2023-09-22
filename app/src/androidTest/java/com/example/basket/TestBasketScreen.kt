package com.example.basket

import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.compose.ui.test.swipeUp
import com.example.basket.di.DatabaseModule
import com.example.basket.entity.TagsTesting.BASKETBOTTOMSHEET
import com.example.basket.entity.TagsTesting.BASKET_BS_INPUT_NAME
import com.example.basket.entity.TagsTesting.BASKET_LAZY
import com.example.basket.entity.TagsTesting.BOTTOM_APP_BAR
import com.example.basket.entity.TagsTesting.BUTTON_OK
import com.example.basket.entity.TagsTesting.DIALOG_EDIT_BASKET
import com.example.basket.entity.TagsTesting.DIALOG_EDIT_BASKET_INPUT_NAME
import com.example.basket.entity.TagsTesting.FAB_PLUS
import com.example.basket.navigation.Baskets
import com.example.basket.ui.MainApp
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
class TestBasketScreen {
    @get : Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get: Rule(order = 1)
    val composeRule = createAndroidComposeRule(MainActivity::class.java)

    private val showBottomSheet = mutableStateOf(false)

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            MainApp()
//            val navController = rememberNavController()
//            AppTheme {
//                NavHost(navController = navController, startDestination = Baskets.route){
//                    composable(route = Baskets.route) {
//                        BasketsScreen(
//                            showBottomSheet = showBottomSheet,
//                            onClickBasket = { navController.navigateToProducts(it) },
//                        )
//                    }
//                    composable(
//                        route = ProductsBasket.routeWithArgs, arguments = ProductsBasket.arguments)
//                    { navBackStackEntry ->
//                        val basketId = navBackStackEntry.arguments?.getLong(ProductsBasket.basketIdArg)
//                        if (basketId != null) {
//                            ProductsScreen(basketId = basketId, showBottomSheet = showBottomSheet,)
//                        }
//                    }
//                    composable(route = Articles.route,) {
//                        ArticlesScreen(showBottomSheet = showBottomSheet)
//                    }
//                    composable(route = Setting.route) { SettingsScreen() }
//                }
//            }
        }
//        composeRule.onRoot(useUnmergedTree = true).printToLog(tag = "KDS")
    }

    @Test
    fun existsLazy() {
        composeRule.onNodeWithTag(BASKET_LAZY).assertExists()
    }

    @Test
    fun existsPicture() {
        composeRule.onNodeWithContentDescription("Photo").assertExists()
        composeRule.onNodeWithContentDescription("Photo").assertIsDisplayed()
    }

    @Test
    fun existsTitle() {
        composeRule.onNodeWithTag(BASKET_LAZY).assertExists()
        composeRule.onNodeWithTag(BASKET_LAZY).assertIsDisplayed()
    }

    @Test
    fun existsBottomAppBar() {
        composeRule.onNodeWithTag(BOTTOM_APP_BAR).assertExists()
        composeRule.onNodeWithTag(BOTTOM_APP_BAR).assertIsDisplayed()
        composeRule.onNodeWithTag(Baskets.route).assertExists()
        composeRule.onNodeWithTag(Baskets.route).assertIsDisplayed()
    }

    @Test
    fun showListBasket() {

        val basket0 = hasText("0 basket") and hasClickAction()
        val basketN = hasText("9 basket") and hasClickAction()
        composeRule.onNodeWithTag(BASKET_LAZY).assertExists()

        composeRule.onNodeWithTag(Baskets.route).performClick()
        composeRule.waitUntil(timeoutMillis = 4000L, condition = { true })

        composeRule.onNodeWithTag(BASKET_LAZY).performTouchInput {
            swipeUp(startY = bottom, endY = top , durationMillis = 200) }
        composeRule.onNode(basket0).assertIsDisplayed()
        composeRule.onNode(basketN).assertIsDisplayed()
        composeRule.onNode(basket0).performClick()
        composeRule.waitUntil(timeoutMillis = 4000L, condition = { true })
    }

    @Test
    fun checkAddBasketFromBottomSheet() {
        val newBasketName = "New Basket Name"
        val basketN = hasText(newBasketName) and hasClickAction()
        val basketN1 = hasText("9 basket") and hasClickAction()

//        composeRule.onRoot(useUnmergedTree = true).printToLog(tag = "KDS")
        composeRule.onNodeWithTag(FAB_PLUS).assertIsDisplayed()
        composeRule.onNodeWithTag(FAB_PLUS).performClick()
        composeRule.waitUntil(timeoutMillis = 4000L, condition = { true })
        composeRule.onNodeWithTag(BASKETBOTTOMSHEET).assertExists()
        composeRule.onNodeWithTag(BASKET_BS_INPUT_NAME).assertExists()
        composeRule.onNodeWithTag(BASKET_BS_INPUT_NAME).performTextClearance()
        composeRule.onNodeWithTag(BASKET_BS_INPUT_NAME).performTextInput(newBasketName)
        composeRule.onNodeWithTag(BUTTON_OK, useUnmergedTree = true).performClick()
        composeRule.onNodeWithTag(BASKET_LAZY).performTouchInput {
            swipeUp(startY = bottom, endY = top , durationMillis = 200) }
        composeRule.waitUntil(timeoutMillis = 4000L, condition = { true })
        composeRule.onNode(basketN1).assertIsDisplayed()
        composeRule.onNode(basketN).assertIsDisplayed()
    }

    @Test fun checkEditBasket(){
        val newBasketName = "New Basket Name"
        val basket0 = hasText("0 basket") and hasClickAction()
        val basketN = hasText(newBasketName) and hasClickAction()

        composeRule.onNodeWithTag(Baskets.route).performClick()
        composeRule.waitUntil(timeoutMillis = 4000L, condition = { true })

        composeRule.onNode(basket0).assertIsDisplayed()
        composeRule.onNode(basket0).performTouchInput { swipeRight(startX = left, endX = right) }
        composeRule.onNodeWithTag(DIALOG_EDIT_BASKET).assertExists()
        composeRule.onNodeWithTag(DIALOG_EDIT_BASKET_INPUT_NAME).assertExists()
        composeRule.onNodeWithTag(DIALOG_EDIT_BASKET_INPUT_NAME).performTextClearance()
        composeRule.onNodeWithTag(DIALOG_EDIT_BASKET_INPUT_NAME).performTextInput(newBasketName)
        composeRule.onNodeWithTag(BUTTON_OK, useUnmergedTree = true).performClick()
        composeRule.onNodeWithTag(BASKET_LAZY).performTouchInput {
            swipeUp(startY = bottom, endY = top , durationMillis = 200) }
        composeRule.waitUntil(timeoutMillis = 4000L, condition = { true })
        composeRule.onNode(basketN).assertIsDisplayed()
    }

    @Test fun checkingDeletionBasket() {

        val basketN = hasText("4 basket") and hasClickAction()

        // Refresh screen for view baskets
        composeRule.onNodeWithTag(Baskets.route).performClick()
        composeRule.waitUntil(timeoutMillis = 4000L, condition = { true })
        //Swipe for view all list basket
        composeRule.onNodeWithTag(BASKET_LAZY).performTouchInput { swipeUp(startY = bottom, endY = top)}
        //Check exists basket "4 basket"
        composeRule.onNode(basketN).assertIsDisplayed()
        //Swipe for delete basket "4 basket"
        composeRule.onNode(basketN).performTouchInput { swipeLeft(startX = right, endX = left) }
        //Refresh screen for view list basket
        composeRule.onNodeWithTag(BASKET_LAZY).performTouchInput { swipeUp(startY = bottom, endY = top)}
        composeRule.waitUntil(timeoutMillis = 4000L, condition = { true })
        //Checking the deletion of the "4 baskets" basket
        composeRule.onNode(basketN).assertIsNotDisplayed()
    }
}