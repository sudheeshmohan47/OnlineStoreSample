package com.sample.onlinestore.presentation.appintro

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.internal.data.model.ScreenData
import com.sample.datastoragemodule.di.PreferenceModule
import com.sample.designsystem.foundation.ui.OnlineStoreTheme
import com.sample.onlinestore.R
import com.sample.onlinestore.foundation.navigation.OnlineStoreScreens
import com.sample.onlinestore.foundation.navigation.navgraph.rootGraph
import com.sample.onlinestore.presentation.appintro.appintro.AppIntroViewModel
import com.sample.onlinestore.presentation.appintro.appintro.screens.AppIntroScreen
import com.sample.onlinestore.presentation.main.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(PreferenceModule::class)
class AppIntroScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var context: Context

    @Before
    fun setup() {
        hiltRule.inject()
        context = composeRule.activity

        composeRule.activity.setContent {
            val navController = rememberNavController()
            OnlineStoreTheme {
                NavHost(
                    navController = navController,
                    startDestination = OnlineStoreScreens.AppIntroScreen,
                ) {

                    composable<OnlineStoreScreens.AppIntroScreen> {
                        AppIntroScreen(
                            goToLoginScreen = {

                            }
                        )
                    }
                }
            }
        }
    }

    @Test
    fun appIntroScreenTest() {

        val firstScreenTitle = context.getString(R.string.app_intro_title_1)
        val secondScreenTitle = context.getString(R.string.app_intro_title_2)
        val thirdScreenTitle = context.getString(R.string.app_intro_title_3)

        val  firstScreenDescription = context.getString(R.string.app_intro_subtitle_1)
        val  secondScreenDescription = context.getString(R.string.app_intro_subtitle_2)
        val  thirdScreenDescription = context.getString(R.string.app_intro_subtitle_3)

        val nextButton = composeRule.onNodeWithTag(context.getString(R.string.test_tag_appintro_next_button))

        composeRule.onNodeWithText(firstScreenTitle).assertIsDisplayed()
        composeRule.onNodeWithText(firstScreenDescription).assertIsDisplayed()
        nextButton.performClick()

        composeRule.onNodeWithText(secondScreenTitle).assertIsDisplayed()
        composeRule.onNodeWithText(secondScreenDescription).assertIsDisplayed()
        nextButton.performClick()

        composeRule.onNodeWithText(thirdScreenTitle).assertIsDisplayed()
        composeRule.onNodeWithText(thirdScreenDescription).assertIsDisplayed()

        nextButton.performClick()
       // nextButton.assertDoesNotExist()
    }
}
