package com.example.compose.rally

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.compose.rally.ui.components.RallyTopAppBar
import org.junit.Rule
import org.junit.Test
import java.util.*

class TopAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun myTest_selected() {
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {

            RallyTopAppBar(allScreens = allScreens, onTabSelected = {}, currentScreen = RallyScreen.Accounts)

        }
        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertIsSelected()

    }

    @Test
    fun myTest_label_exist() {
        
        composeTestRule.setContent {
            val allScreen = RallyScreen.values().toList()
            RallyTopAppBar( allScreens = allScreen, onTabSelected = { }, currentScreen = RallyScreen.Accounts )
            
        }

        composeTestRule
            .onNode(
                hasText(RallyScreen.Accounts.name.uppercase(Locale.getDefault())) and
                        hasParent(
                            hasContentDescription(RallyScreen.Accounts.name)
                        ),
                useUnmergedTree = true
            )
            .assertExists()

    }



}