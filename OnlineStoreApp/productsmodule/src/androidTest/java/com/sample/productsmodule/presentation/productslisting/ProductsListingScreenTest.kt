package com.sample.productsmodule.presentation.productslisting

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sample.datastoragemodule.di.PreferenceModule
import com.sample.designsystem.foundation.ui.OnlineStoreTheme
import com.sample.onlinestore.commonmodule.presentation.test.TestActivity
import com.sample.onlinestore.productsmodule.foundation.navigation.ProductScreens
import com.sample.onlinestore.productsmodule.presentation.productslisting.screen.ProductsListingScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(PreferenceModule::class)
class ProductsListingScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<TestActivity>()

    private lateinit var context: Context

    @Before
    fun setup() {
        hiltRule.inject()
        context = composeRule.activity

        composeRule.setContent {
            val navController = rememberNavController()
            OnlineStoreTheme {
                NavHost(
                    navController = navController,
                    startDestination = ProductScreens.ProductListingScreen,
                ) {
                    composable<ProductScreens.ProductListingScreen> {
                        ProductsListingScreen(
                            loadProductDetailScreen = {},
                            gotoCartScreen = {},
                        )
                    }
                }
            }
        }
    }

    @Test
    fun productListing_TitleCheck() {
        composeRule.onNodeWithText("Home").assertIsDisplayed()
    }
}