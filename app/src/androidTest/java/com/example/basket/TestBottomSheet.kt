package com.example.basket

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.core.app.ActivityScenario
import com.example.basket.di.DatabaseModule
import com.example.basket.entity.TagsTesting
import com.example.basket.entity.TagsTesting.BASKETBOTTOMSHEET
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
class TestBottomSheet {
    @get : Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get: Rule(order = 1)
    val alertDialogRule = createEmptyComposeRule()
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        hiltRule.inject()
        scenario = ActivityScenario.launch(MainActivity::class.java)
        alertDialogRule.onRoot(useUnmergedTree = true).printToLog(tag = "KDS")
    }

    @Test
    fun runBottomSheet(){
        alertDialogRule.onNodeWithTag(TagsTesting.FAB_PLUS).assertIsDisplayed()
        alertDialogRule.onNodeWithTag(TagsTesting.FAB_PLUS).performClick()
        alertDialogRule.onNodeWithTag(BASKETBOTTOMSHEET).assertExists()
    }
}