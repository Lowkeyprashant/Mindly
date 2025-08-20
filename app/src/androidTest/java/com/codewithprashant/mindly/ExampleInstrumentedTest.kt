package com.codewithprashant.mindly

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.codewithprashant.mindly.ui.theme.MindlyTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.codewithprashant.mindly.ui.MindlyNavGraph

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.codewithprashant.mindly", appContext.packageName)
    }

    @Test
    fun mindlyNavGraphIsDisplayed() {
        composeTestRule.setContent {
            MindlyTheme {
                MindlyNavGraph()
            }
        }
        composeTestRule.onNodeWithText("Mindly App - Firebase Test Running!").assertExists()
    }
}