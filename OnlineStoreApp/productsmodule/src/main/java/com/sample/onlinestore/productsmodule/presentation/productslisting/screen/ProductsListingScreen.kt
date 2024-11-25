package com.sample.onlinestore.productsmodule.presentation.productslisting.screen

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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.designsystem.components.OnlineStorePullToRefreshBox
import com.sample.designsystem.components.ShowDashedProgressIndicator
import com.sample.designsystem.foundation.OnlineStoreSnackBarHost
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.commonmodule.utils.handleErrorMessage
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingAction
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingEvent
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingUiModel
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingViewModel
import com.sample.onlinestore.productsmodule.presentation.productslisting.productsListingViewModelCreationCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsListingScreen(
    loadProductDetailScreen: (String) -> Unit,
    gotoCartScreen: () -> Unit,
    modifier: Modifier = Modifier,
    productListingViewModel: ProductsListingViewModel = hiltViewModel(
        creationCallback = productsListingViewModelCreationCallback
    )
) {
    val productsListingUiState by productListingViewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val topAppBarState = rememberTopAppBarState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val pullRefreshState = rememberPullToRefreshState()
    val productListState: LazyGridState = rememberLazyGridState()
    val shimmerEffectGridState: LazyGridState = rememberLazyGridState()

    LifecycleResumeEffect(Unit) {
        productListingViewModel.sendAction(ProductsListingAction.RefreshData)
        onPauseOrDispose { }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        ProductListingMainContent(
            productsListingUiState = productsListingUiState,
            screenWidth = screenWidth,
            onAction = {
                productListingViewModel.sendAction(it)
            },
            pullToRefreshState = pullRefreshState,
            topAppBarState = topAppBarState,
            productListState = productListState,
            shimmerEffectGridState = shimmerEffectGridState
        )
        OnlineStoreSnackBarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        HandleUIStateChanges(
            productsListingUiState = productsListingUiState,
            snackBarHostState = snackBarHostState,
            loadDetailScreen = loadProductDetailScreen,
            gotoCartScreen = gotoCartScreen
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListingMainContent(
    productsListingUiState: UiState<ProductsListingUiModel>,
    screenWidth: Dp,
    onAction: (ProductsListingAction) -> Unit,
    modifier: Modifier = Modifier,
    pullToRefreshState: PullToRefreshState = rememberPullToRefreshState(),
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    productListState: LazyGridState = rememberLazyGridState(),
    shimmerEffectGridState: LazyGridState = rememberLazyGridState()
) {
    val isRefreshing = productsListingUiState.data?.isSwipeRefreshing ?: false

    OnlineStorePullToRefreshBox(
        pullRefreshState = pullToRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = {
            onAction(
                ProductsListingAction.SetSwipeRefreshingStatus(
                    true
                )
            )
            onAction(ProductsListingAction.RefreshData)
        },
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ProductsListingTopAppBarSection(
                topAppBarState = topAppBarState,
                onAction = onAction
            )
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            ProductsListingScreenContent(
                productsListingUiState = productsListingUiState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = OnlineStoreSpacing.MEDIUM.dp()),
                screenWidth = screenWidth,
                onAction = onAction,
                productListState = productListState,
                shimmerEffectGridState = shimmerEffectGridState
            )
        }
    }
}

@Composable
private fun HandleUIStateChanges(
    productsListingUiState: UiState<ProductsListingUiModel>,
    snackBarHostState: SnackbarHostState,
    loadDetailScreen: (String) -> Unit,
    gotoCartScreen: () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    productListingViewModel: ProductsListingViewModel = hiltViewModel(
        creationCallback = productsListingViewModelCreationCallback
    )
) {
    val context = LocalContext.current
    val loadDetailScreenScreenState by rememberUpdatedState(loadDetailScreen)
    val gotoCartScreenState by rememberUpdatedState(gotoCartScreen)
    val isSwipeRefreshing = productsListingUiState.data?.isSwipeRefreshing ?: false
    val isInitialLoadingCompleted = productsListingUiState.data?.isInitialLoadingCompleted ?: false

    LaunchedEffect(Unit) {
        productListingViewModel.uiEvent.collectLatest { event ->
            when (event) {
                is ProductsListingEvent.LoadProductDetailScreen -> {
                    loadDetailScreenScreenState(event.productId)
                }

                is ProductsListingEvent.GotoCartScreen -> {
                    gotoCartScreenState()
                }

                is ProductsListingEvent.ShowMessage -> {
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
        productsListingUiState is UiState.Loading &&
        !isSwipeRefreshing
    ) {
        ShowDashedProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true, device = Devices.PIXEL_XL)
private fun ProductsListingScreenUIPreview() {
    ProductListingMainContent(
        productsListingUiState = UiState.Result(
            ProductsListingUiModel(
                products = listOf(),
                isSwipeRefreshing = false
            )
        ),
        screenWidth = 300.dp,
        onAction = {}
    )
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_XL)
private fun ProductsListingScreenUIPreviewDark() {
    ProductsListingScreenUIPreview()
}
