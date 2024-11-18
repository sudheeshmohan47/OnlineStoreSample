package com.sample.onlinestore.productsmodule.presentation.productslisting.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.atlas.library.foundations.AtlasSpacing
import com.atlas.library.foundations.dp
import com.carelo.android.foundation.base.UiState
import com.carelo.android.foundation.ui.customui.ShowDashedProgressIndicator
import com.carelo.android.presentation.dashboard.collection.CollectionsEvent
import com.carelo.android.presentation.dashboard.collection.CollectionsUiModel
import com.carelo.android.presentation.dashboard.collection.CollectionsViewModel
import com.carelo.android.presentation.dashboard.collection.collectionsViewModelCreationCallback
import com.carelo.android.utils.handleErrorMessage
import com.sample.designsystem.components.OnlineStorePullToRefreshBox
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingAction
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingViewModel
import com.sample.onlinestore.productsmodule.presentation.productslisting.productsListingViewModelCreationCallback
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsListingScreen(
    loadProductDetailScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    productListingViewModel: ProductsListingViewModel = hiltViewModel(creationCallback = productsListingViewModelCreationCallback)
) {
    val productsListingUiState by productListingViewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val topAppBarState = rememberTopAppBarState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val pullRefreshState = rememberPullToRefreshState()
    val isRefreshing = productsListingUiState.data?.isSwipeRefreshing ?: false

    OnlineStorePullToRefreshBox(
        pullRefreshState = pullRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = {
            productListingViewModel.sendAction(
                ProductsListingAction.SetSwipeRefreshingStatus(
                    true
                )
            )
            productListingViewModel.sendAction(ProductsListingAction.RefreshData)
        },
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
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

            CollectionsScreenContent(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = AtlasSpacing.MEDIUM.dp()),
                collectionsUiState = collectionsUiState,
                screenWidth = screenWidth,
                onAction = {
                    collectionViewModel.sendAction(it)
                }
            )
        }
        HandleUIStateChanges(
            collectionsUiState = collectionsUiState,
            snackBarHostState = snackBarHostState,
            loadViewAllCollectionsScreen = loadViewAllCollectionsScreen,
            loadDetailScreen = loadDetailScreen
        )
    }
}

@Composable
private fun HandleUIStateChanges(
    collectionsUiState: UiState<CollectionsUiModel>,
    snackBarHostState: SnackbarHostState,
    loadViewAllCollectionsScreen: (String) -> Unit,
    loadDetailScreen: (String) -> Unit,
    collectionViewModel: CollectionsViewModel = hiltViewModel(creationCallback = collectionsViewModelCreationCallback)
) {
    val context = LocalContext.current
    val loadViewAllCollectionsScreenState by rememberUpdatedState(loadViewAllCollectionsScreen)
    val loadDetailScreenScreenState by rememberUpdatedState(loadDetailScreen)

    LaunchedEffect(collectionsUiState) {
        when (collectionsUiState) {
            is UiState.Result -> {
                collectionsUiState.errorMessage?.let {
                    handleErrorMessage(
                        context = context,
                        snackBarHostState = snackBarHostState,
                        coroutineScope = this,
                        errorMessage = it
                    )
                }
            }

            is UiState.Loading -> {}

            is UiState.Error -> {}
        }
    }

    LaunchedEffect(Unit) {
        collectionViewModel.uiEvent.collectLatest { event ->
            when (event) {

                is CollectionsEvent.LoadViewAllCollectionsScreen -> {
                    loadViewAllCollectionsScreenState(event.collectionType)
                }

                CollectionsEvent.EndUserSession -> {
                    // TODO End user session
                }

                is CollectionsEvent.LoadDetailsScreen -> {
                    loadDetailScreenScreenState(event.id)
                }
            }
        }
    }
    if (collectionsUiState is UiState.Loading) {
        ShowDashedProgressIndicator()
    }
}
