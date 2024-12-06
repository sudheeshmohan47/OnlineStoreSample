package com.sample.productsmodule.presentation.screenshot

import androidx.compose.material3.ExperimentalMaterial3Api
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.sample.designsystem.foundation.ui.OnlineStoreTestAndroidTheme
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.productsmodule.domain.model.ProductItem
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingUiModel
import com.sample.onlinestore.productsmodule.presentation.productslisting.screen.ProductListingMainContent
import com.sample.productsmodule.MainCoroutineRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductsListingScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        showSystemUi = false,
        maxPercentDifference = 1.0,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutineRule: MainCoroutineRule = MainCoroutineRule()

    private val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun productsListingScreenTest_lightTheme() {
        paparazzi.snapshot {
            OnlineStoreTestAndroidTheme {
                val uiState = UiState.Result(
                    ProductsListingUiModel(
                        products = getFakeProductItems()
                    )
                )
                ProductListingMainContent(
                    productsListingUiState = uiState,
                    onAction = {}
                )
            }
        }
    }
}

fun getFakeProductItems(): List<ProductItem> {
    val products = mutableListOf<ProductItem>()
    for (i in 1..10) {
        products.add(
            ProductItem(
                productId = i.toString(),
                name = "Product $i",
                price = i * 10.0,
                category = "Category ${(i % 3) + 1}",
                description = "Description for Product $i",
                image = "https://picsum.photos/seed/picsum/200/300",
                isWishListed = (i % 2 == 0),
                isAddedToCart = (i % 3 == 0)
            )
        )
    }
    return products
}
