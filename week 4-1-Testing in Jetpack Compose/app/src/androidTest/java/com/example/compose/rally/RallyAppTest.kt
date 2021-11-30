package com.example.compose.rally

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class RallyAppTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun rallyAppTest() {
        val screenList = RallyScreen.values().toList()
        var currentScreen = RallyScreen.Overview
        composeTestRule.setContent {

            RallyApp( currentScreen ) {

               currentScreen = it

            }

        }

        composeTestRule.onRoot().printToLog("currentLabelExists")

        screenList.forEach {

            composeTestRule
                .onNodeWithContentDescription( it.name )
                .performClick()
            assert( currentScreen == it )

        }

    }

}