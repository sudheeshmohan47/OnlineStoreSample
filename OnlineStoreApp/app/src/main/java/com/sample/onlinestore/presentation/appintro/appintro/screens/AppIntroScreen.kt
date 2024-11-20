package com.sample.onlinestore.presentation.appintro.appintro.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.sample.designsystem.components.OnlineStoreButton
import com.sample.designsystem.foundation.OnlineStoreSize
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp
import com.sample.onlinestore.R
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.commonmodule.foundation.navigation.customcomponents.SlidingDotsPagerIndicator
import com.sample.onlinestore.presentation.appintro.appintro.AppIntroAction
import com.sample.onlinestore.presentation.appintro.appintro.AppIntroEvent
import com.sample.onlinestore.presentation.appintro.appintro.AppIntroPage
import com.sample.onlinestore.presentation.appintro.appintro.AppIntroUiModel
import com.sample.onlinestore.presentation.appintro.appintro.AppIntroViewModel
import com.sample.onlinestore.presentation.appintro.appintro.appIntroViewModelCreationCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val AppIntroPageCount = 3

@Composable
fun AppIntroScreen(
    goToLoginScreen: () -> Unit,
    modifier: Modifier = Modifier,
    appIntroViewModel: AppIntroViewModel = hiltViewModel(creationCallback = appIntroViewModelCreationCallback)
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        pageCount = {
            AppIntroPageCount
        }
    )
    val uiState: UiState<AppIntroUiModel> = appIntroViewModel.uiState.collectAsState().value

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LoadIntroContent(
            modifier = Modifier,
            uiModel = uiState.data,
            coroutineScope = coroutineScope,
            pagerState = pagerState
        ) {
            appIntroViewModel.sendAction(it)
        }
        HandleUiState(goToLoginScreen = goToLoginScreen)
    }
}

@Composable
fun LoadIntroContent(
    uiModel: AppIntroUiModel?,
    coroutineScope: CoroutineScope,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    onAction: (AppIntroAction) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        uiModel?.appIntroPages?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                // Pager for showing onboard screens
                HorizontalPager(
                    state = pagerState,
                    verticalAlignment = Alignment.Top
                ) { position ->
                    AppIntroPageContent(appIntroPage = uiModel.appIntroPages[position])
                }
            }
            // Sliding dots Pager indicator for showing current page
            SlidingDotsPagerIndicator(
                pagerState = pagerState
            )
            Spacer(modifier = Modifier.height(OnlineStoreSpacing.LARGE.dp()))

            // Action button for navigating through pages and for submitting
            OnlineStoreButton(
                buttonTextColor = MaterialTheme.colorScheme.onPrimary,
                label = stringResource(R.string.label_next),
                modifier = Modifier.padding(
                    horizontal = OnlineStoreSpacing.MEDIUM.dp(),
                    vertical = OnlineStoreSize.EXTRA_SMALL.dp()
                ),
                buttonStyle = MaterialTheme.typography.titleMedium
            ) {
                if (pagerState.currentPage < AppIntroPageCount - 1) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    onAction(AppIntroAction.LoadNextScreen)
                }
            }
        }
    }
}

@Composable
private fun HandleUiState(
    goToLoginScreen: () -> Unit,
    appIntroViewModel: AppIntroViewModel = hiltViewModel(creationCallback = appIntroViewModelCreationCallback)
) {
    val goToLoginScreenState by rememberUpdatedState(goToLoginScreen)
    LaunchedEffect(Unit) {
        appIntroViewModel.uiEvent.collectLatest { event ->
            when (event) {
                is AppIntroEvent.LoadNextScreen -> {
                    goToLoginScreenState()
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, device = Devices.PIXEL_XL)
private fun OnBoardContentUIPreview() {
    LoadDummyContent()
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, device = Devices.PIXEL_XL)
private fun OnBoardContentUIDarkPreview() {
    LoadDummyContent()
}

@Composable
private fun LoadDummyContent(modifier: Modifier = Modifier) {
    LoadIntroContent(
        modifier = modifier,
        uiModel = AppIntroUiModel(getDummyList()),
        coroutineScope = rememberCoroutineScope(),
        pagerState = rememberPagerState(
            pageCount = {
                AppIntroPageCount
            }
        )
    ) {}
}

private fun getDummyList(): List<AppIntroPage> = listOf(
    AppIntroPage(
        imageId = R.drawable.img_app_intro_2,
        titleResId = R.string.app_intro_title_1,
        subTitleResId = R.string.app_intro_subtitle_1
    )
)
