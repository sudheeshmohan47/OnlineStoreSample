package com.sample.onlinestore.categoriesmodule.presentation.categories.screen

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.designsystem.components.OnlineStorePullToRefreshBox
import com.sample.designsystem.components.ShowDashedProgressIndicator
import com.sample.designsystem.foundation.OnlineStoreSnackBarHost
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp
import com.sample.onlinestore.categoriesmodule.presentation.categories.CategoriesAction
import com.sample.onlinestore.categoriesmodule.presentation.categories.CategoriesEvent
import com.sample.onlinestore.categoriesmodule.presentation.categories.CategoriesUiModel
import com.sample.onlinestore.categoriesmodule.presentation.categories.CategoriesViewModel
import com.sample.onlinestore.categoriesmodule.presentation.categories.categoriesViewModelCreationCallback
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.commonmodule.utils.handleErrorMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    categoriesViewModel: CategoriesViewModel = hiltViewModel(
        creationCallback = categoriesViewModelCreationCallback
    )
) {
    val categoriesUiState by categoriesViewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val topAppBarState = rememberTopAppBarState()
    val pullRefreshState = rememberPullToRefreshState()
    val productListState: LazyGridState = rememberLazyGridState()
    val shimmerEffectGridState: LazyGridState = rememberLazyGridState()
    val context = LocalContext.current

    LifecycleResumeEffect(Unit) {
        categoriesViewModel.sendAction(CategoriesAction.RefreshData)
        onPauseOrDispose { }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        CategoriesMainContent(
            categoriesUiState = categoriesUiState,
            onAction = {
                categoriesViewModel.sendAction(it)
            },
            pullToRefreshState = pullRefreshState,
            topAppBarState = topAppBarState,
            categoriesListState = productListState,
            shimmerEffectGridState = shimmerEffectGridState
        )
        OnlineStoreSnackBarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        HandleUIStateChanges(
            categoriesUiState = categoriesUiState,
            snackBarHostState = snackBarHostState,
            context = context
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesMainContent(
    categoriesUiState: UiState<CategoriesUiModel>,
    onAction: (CategoriesAction) -> Unit,
    modifier: Modifier = Modifier,
    pullToRefreshState: PullToRefreshState = rememberPullToRefreshState(),
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    categoriesListState: LazyGridState = rememberLazyGridState(),
    shimmerEffectGridState: LazyGridState = rememberLazyGridState()
) {
    val isRefreshing = categoriesUiState.data?.isSwipeRefreshing ?: false

    OnlineStorePullToRefreshBox(
        pullRefreshState = pullToRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = {
            onAction(
                CategoriesAction.SetSwipeRefreshingStatus(
                    true
                )
            )
            onAction(CategoriesAction.RefreshData)
        },
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ProductsListingTopAppBarSection(
                topAppBarState = topAppBarState
            )
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            CategoriesScreenListingContent(
                categoriesUiState = categoriesUiState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = OnlineStoreSpacing.MEDIUM.dp()),
                onAction = onAction,
                categoriesListState = categoriesListState,
                shimmerEffectGridState = shimmerEffectGridState
            )
        }
    }
}

@Composable
private fun HandleUIStateChanges(
    categoriesUiState: UiState<CategoriesUiModel>,
    snackBarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    categoriesViewModel: CategoriesViewModel = hiltViewModel(
        creationCallback = categoriesViewModelCreationCallback
    ),
    context: Context = LocalContext.current
) {
    val isSwipeRefreshing = categoriesUiState.data?.isSwipeRefreshing ?: false
    val isInitialLoadingCompleted = categoriesUiState.data?.isInitialLoadingCompleted ?: false

    LaunchedEffect(Unit) {
        categoriesViewModel.uiEvent.collectLatest { event ->
            when (event) {
                is CategoriesEvent.ShowMessage -> {
                    handleErrorMessage(
                        context = context,
                        snackBarHostState = snackBarHostState,
                        coroutineScope = coroutineScope,
                        errorMessage = event.message
                    )
                }
            }
        }
    }

    if (isInitialLoadingCompleted && // we will show shimmer effect when loading data for first time
        categoriesUiState is UiState.Loading &&
        !isSwipeRefreshing
    ) {
        ShowDashedProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true, device = Devices.PIXEL_XL)
private fun CategoriesScreenUIPreview() {
    CategoriesMainContent(
        categoriesUiState = UiState.Result(
            CategoriesUiModel(
                categories = listOf(),
                isSwipeRefreshing = false
            )
        ),
        onAction = {}
    )
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_XL)
private fun CategoriesScreenUIPreviewDark() {
    CategoriesScreenUIPreview()
}
