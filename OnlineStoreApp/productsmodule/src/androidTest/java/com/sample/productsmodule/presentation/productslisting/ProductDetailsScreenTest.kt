package com.sample.productsmodule.presentation.productslisting

import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sample.datastoragemodule.di.PreferenceModule
import com.sample.designsystem.foundation.ui.OnlineStoreTheme
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.commonmodule.presentation.test.TestActivity
import com.sample.onlinestore.productsmodule.di.ProductsModule
import com.sample.onlinestore.productsmodule.domain.ProductsUseCase
import com.sample.onlinestore.productsmodule.foundation.navigation.ProductScreens
import com.sample.onlinestore.productsmodule.presentation.productdetails.ProductDetailsUiModel
import com.sample.onlinestore.productsmodule.presentation.productdetails.ProductDetailsViewModel
import com.sample.onlinestore.productsmodule.presentation.productdetails.screen.ProductDetailsScreen
import com.sample.productsmodule.domain.FakeProductRepository
import com.sample.productsmodule.domain.getFakeProducts
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
@UninstallModules(PreferenceModule::class, ProductsModule::class)
class ProductDetailsScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<TestActivity>()

    private lateinit var context: Context
    private lateinit var viewModel: ProductDetailsViewModel
    private lateinit var productsUseCase: ProductsUseCase
    private lateinit var productsRepository: FakeProductRepository

    @Before
    fun setup() {
        hiltRule.inject()
        context = composeRule.activity
        setupEnvironment()
    }

    private fun setupEnvironment() {
        productsRepository = FakeProductRepository()
        productsRepository.products = getFakeProducts()
        productsUseCase = ProductsUseCase(productsRepository)
        viewModel = ProductDetailsViewModel(
            productsUseCase = productsUseCase,
            initialScreenState = UiState.Loading(ProductDetailsUiModel()),
            productId = "1"
        )
        setComposeContent()
    }

    private fun setComposeContent() {
        composeRule.setContent {
            val navController = rememberNavController()
            OnlineStoreTheme {
                NavHost(
                    navController = navController,
                    startDestination = ProductScreens.ProductListingScreen,
                ) {
                    composable<ProductScreens.ProductListingScreen> {
                        ProductDetailsScreen(
                            backToPreviousScreen = {},
                            gotoCartScreen = {},
                            productDetailsViewModel = viewModel,
                            productId = "1"
                        )
                    }
                }
            }
        }
    }
}
