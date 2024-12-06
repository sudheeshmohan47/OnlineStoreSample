package com.sample.productsmodule.presentation.screenshot

import androidx.compose.material3.ExperimentalMaterial3Api
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.sample.designsystem.foundation.ui.OnlineStoreTestAndroidTheme
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.productsmodule.presentation.productdetails.ProductDetailsUiModel
import com.sample.onlinestore.productsmodule.presentation.productdetails.screen.ProductDetailsScreenMainContent
import com.sample.productsmodule.MainCoroutineRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductDetailsScreenshotTest {

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
    fun productsDetailScreenTest_lightTheme() {
        paparazzi.snapshot {
            OnlineStoreTestAndroidTheme {
                val uiState = UiState.Result(
                    ProductDetailsUiModel(
                        product = getFakeProductItems()[0]
                    )
                )
                ProductDetailsScreenMainContent(
                    productDetailsUiState = uiState,
                    onAction = {}
                )
            }
        }
    }
}
