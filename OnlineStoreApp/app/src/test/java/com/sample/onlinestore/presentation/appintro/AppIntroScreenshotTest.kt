package com.sample.onlinestore.presentation.appintro

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.rememberCoroutineScope
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.sample.designsystem.foundation.ui.OnlineStoreTestAndroidTheme
import com.sample.onlinestore.R
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.presentation.appintro.appintro.AppIntroPage
import com.sample.onlinestore.presentation.appintro.appintro.AppIntroUiModel
import com.sample.onlinestore.presentation.appintro.appintro.screens.AppIntroPageCount
import com.sample.onlinestore.presentation.appintro.appintro.screens.LoadAppIntroContent
import org.junit.Rule
import org.junit.Test

class AppIntroScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        showSystemUi = false,
        maxPercentDifference = 1.0,
    )

    @Test
    fun launchSearchScreen_lightTheme() {
        paparazzi.snapshot {
            OnlineStoreTestAndroidTheme {
                val uiState = UiState.Loading(
                    AppIntroUiModel(
                        appIntroPages = getOnBoardPages()
                    )
                )
                val coroutineScope = rememberCoroutineScope()
                val pagerState = rememberPagerState(
                    pageCount = {
                        AppIntroPageCount
                    }
                )
                LoadAppIntroContent(
                    uiModel = uiState.data,
                    coroutineScope = coroutineScope,
                    pagerState = pagerState,
                    onAction = {}
                )
            }
        }
    }

    // Returns the list of pages for the horizontal pager
    private fun getOnBoardPages(): List<AppIntroPage> {
        return listOf(
            AppIntroPage(
                imageId = R.drawable.img_app_intro_1,
                titleResId = R.string.app_intro_title_1,
                subTitleResId = R.string.app_intro_subtitle_1,
            ),
            AppIntroPage(
                imageId = R.drawable.img_app_intro_2,
                titleResId = R.string.app_intro_title_2,
                subTitleResId = R.string.app_intro_subtitle_2,
            ),
            AppIntroPage(
                imageId = R.drawable.img_app_intro_3,
                titleResId = R.string.app_intro_title_3,
                subTitleResId = R.string.app_intro_subtitle_3,
            )
        )
    }
}
