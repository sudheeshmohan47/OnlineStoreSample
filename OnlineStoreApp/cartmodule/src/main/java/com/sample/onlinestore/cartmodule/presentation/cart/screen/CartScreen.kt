package com.sample.onlinestore.cartmodule.presentation.cart.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarState
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
import com.sample.designsystem.components.ShowDashedProgressIndicator
import com.sample.designsystem.foundation.OnlineStoreSnackBarHost
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp
import com.sample.onlinestore.cartmodule.presentation.cart.CartAction
import com.sample.onlinestore.cartmodule.presentation.cart.CartEvent
import com.sample.onlinestore.cartmodule.presentation.cart.CartUiModel
import com.sample.onlinestore.cartmodule.presentation.cart.CartViewModel
import com.sample.onlinestore.cartmodule.presentation.cart.cartViewModelCreationCallback
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.commonmodule.utils.handleErrorMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    loadProductDetailScreen: (String) -> Unit,
    backToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel = hiltViewModel(
        creationCallback = cartViewModelCreationCallback
    )
) {
    val cartUiState by cartViewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val topAppBarState = rememberTopAppBarState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cartItemsState: LazyListState = rememberLazyListState()

    LifecycleResumeEffect(Unit) {
        cartViewModel.sendAction(CartAction.RefreshData)
        onPauseOrDispose { }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        CartMainContent(
            cartUiState = cartUiState,
            screenWidth = screenWidth,
            onAction = {
                cartViewModel.sendAction(it)
            },
            topAppBarState = topAppBarState,
            cartItemsState = cartItemsState,
        )
        OnlineStoreSnackBarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        HandleUIStateChanges(
            cartUiState = cartUiState,
            snackBarHostState = snackBarHostState,
            loadDetailScreen = loadProductDetailScreen,
            backToPreviousScreen = backToPreviousScreen
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartMainContent(
    cartUiState: UiState<CartUiModel>,
    screenWidth: Dp,
    onAction: (CartAction) -> Unit,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    cartItemsState: LazyListState = rememberLazyListState(),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        CartTopAppBarSection(
            topAppBarState = topAppBarState,
            onAction = onAction
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        CartScreenContent(
            cartUiState = cartUiState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = OnlineStoreSpacing.MEDIUM.dp()),
            screenWidth = screenWidth,
            onAction = onAction,
            cartListState = cartItemsState
        )
    }
}

@Composable
private fun HandleUIStateChanges(
    cartUiState: UiState<CartUiModel>,
    snackBarHostState: SnackbarHostState,
    loadDetailScreen: (String) -> Unit,
    backToPreviousScreen: () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    cartViewModel: CartViewModel = hiltViewModel(
        creationCallback = cartViewModelCreationCallback
    )
) {
    val context = LocalContext.current
    val loadDetailScreenState by rememberUpdatedState(loadDetailScreen)
    val backToPreviousScreenState by rememberUpdatedState(backToPreviousScreen)

    LaunchedEffect(Unit) {
        cartViewModel.uiEvent.collectLatest { event ->
            when (event) {
                is CartEvent.LoadProductDetailScreen -> {
                    loadDetailScreenState(event.productId)
                }

                is CartEvent.ShowMessage -> {
                    handleErrorMessage(
                        context = context,
                        snackBarHostState = snackBarHostState,
                        coroutineScope = coroutineScope,
                        errorMessage = event.message
                    )
                }

                CartEvent.BackToPreviousScreen -> backToPreviousScreenState()
            }
        }
    }
    if (cartUiState is UiState.Loading) {
        ShowDashedProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true, device = Devices.PIXEL_XL)
private fun CartScreenUIPreview() {
    CartMainContent(
        cartUiState = UiState.Result(
            CartUiModel(
                cartItems = listOf(
                    com.sample.onlinestore.cartmodule.domain.model.CartItem(
                        productId = "1",
                        name = "Product 1",
                        image = "",
                        quantity = 1,
                        price = 22.0,
                        description = "",
                        category = "electronics"
                    )
                )
            )
        ),
        screenWidth = 300.dp,
        onAction = {}
    )
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_XL)
private fun CartScreenUIPreviewDark() {
    CartScreenUIPreview()
}
