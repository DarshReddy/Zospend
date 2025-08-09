package com.assignment.zospend.ui.entry

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.assignment.zospend.ui.theme.ZospendTheme
import org.junit.Rule
import org.junit.Test

class EntryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun validationError_displaysErrorMessage() {
        composeTestRule.setContent {
            ZospendTheme {
                EntryScreen(onNavigateBack = {})
            }
        }

        composeTestRule.onNodeWithText("Title").performTextInput("Test Expense")
        composeTestRule.onNodeWithText("Amount").performTextInput("0")
        composeTestRule.onNodeWithText("Save Expense").performClick()
        composeTestRule.onNodeWithText("Amount must be a valid number greater than 0")
            .assertIsDisplayed()
    }
}
