package com.sample.productsmodule.presentation.productslisting

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.performTouchInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.action.ViewActions.swipeDown
import com.sample.datastoragemodule.di.PreferenceModule
import com.sample.designsystem.foundation.ui.OnlineStoreTheme
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.commonmodule.presentation.test.TestActivity
import com.sample.onlinestore.productsmodule.di.ProductsModule
import com.sample.onlinestore.productsmodule.domain.ProductsUseCase
import com.sample.onlinestore.productsmodule.foundation.navigation.ProductScreens
import com.sample.onlinestore.productsmodule.presentation.productdetails.ProductDetailsViewModel
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingUiModel
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingViewModel
import com.sample.onlinestore.productsmodule.presentation.productslisting.screen.ProductsListingScreen
import com.sample.onlinestore.productsmodule.util.ProductTestTags
import com.sample.productsmodule.domain.FakeProductRepository
import com.sample.productsmodule.domain.getFakeProducts
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(PreferenceModule::class, ProductsModule::class)
class ProductsListingScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<TestActivity>()

    private lateinit var context: Context
    private lateinit var viewModel: ProductsListingViewModel
    private lateinit var productsUseCase: ProductsUseCase
    private lateinit var productsRepository: FakeProductRepository

    @Before
    fun setup() {
        hiltRule.inject()
        context = composeRule.activity
        setupEnvironment()
    }

    private fun setupEnvironment(){
        productsRepository = FakeProductRepository()
        productsRepository.products = getFakeProducts()
        productsUseCase = ProductsUseCase(productsRepository)
        viewModel = ProductsListingViewModel(
            productsUseCase = productsUseCase,
            initialScreenState = UiState.Loading(ProductsListingUiModel()),
        )
        setComposeContent()
    }

    private fun setComposeContent(){
        composeRule.setContent {
            val navController = rememberNavController()
            OnlineStoreTheme {
                NavHost(
                    navController = navController,
                    startDestination = ProductScreens.ProductListingScreen,
                ) {
                    composable<ProductScreens.ProductListingScreen> {
                        ProductsListingScreen(
                            productListingViewModel = viewModel,
                            loadProductDetailScreen = {},
                            gotoCartScreen = {},
                        )
                    }
                }
            }
        }
    }

    private fun setupSuccessEnvironment(){
        productsRepository = FakeProductRepository()
        productsRepository.products = getFakeProducts()
        productsUseCase = ProductsUseCase(productsRepository)
        viewModel = ProductsListingViewModel(
            productsUseCase = productsUseCase,
            initialScreenState = UiState.Loading(ProductsListingUiModel()),
        )
    }

    private fun setupEmptyProductsEnvironment(){
        productsRepository = FakeProductRepository()
        productsUseCase = ProductsUseCase(productsRepository)
        viewModel = ProductsListingViewModel(
            productsUseCase = productsUseCase,
            initialScreenState = UiState.Loading(ProductsListingUiModel()),
        )
    }

    @Test
    fun productListing_TitleCheck() {
        composeRule.onNodeWithText("Home").assertIsDisplayed()
    }

    @Test
    fun productsListing_EnsureListIsDisplayed() {
        composeRule.onNodeWithTag(ProductTestTags.PRODUCT_LISTING_LIST).assertIsDisplayed()
    }

    @Test
    fun productsListing_EnsureListIsNotEmpty() {
        composeRule.onAllNodesWithTag(ProductTestTags.PRODUCT_LISTING_ITEM)[0].assertIsDisplayed()
    }
}
