package com.sample.onlinestore.productsmodule.presentation.productdetails.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.designsystem.components.ShowDashedProgressIndicator
import com.sample.designsystem.foundation.OnlineStoreSnackBarHost
import com.sample.onlinestore.commonmodule.domain.model.Message
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.commonmodule.utils.handleErrorMessage
import com.sample.onlinestore.productsmodule.domain.model.ProductItem
import com.sample.onlinestore.productsmodule.presentation.productdetails.ProductDetailsAction
import com.sample.onlinestore.productsmodule.presentation.productdetails.ProductDetailsEvent
import com.sample.onlinestore.productsmodule.presentation.productdetails.ProductDetailsUiModel
import com.sample.onlinestore.productsmodule.presentation.productdetails.ProductDetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    backToPreviousScreen: () -> Unit,
    gotoCartScreen: () -> Unit,
    modifier: Modifier = Modifier,
    productId: String,
    productDetailsViewModel: ProductDetailsViewModel =
        hiltViewModel<ProductDetailsViewModel,
                ProductDetailsViewModel.ProductDetailsViewModelFactory> { factory ->
            factory.create(productId = productId)
        }
) {
    val productDetailsUiState by productDetailsViewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val topAppBarState = rememberTopAppBarState()
    val productDetailsListState: LazyListState = rememberLazyListState()

    LifecycleResumeEffect(Unit) {
        productDetailsViewModel.sendAction(ProductDetailsAction.RefreshData)
        onPauseOrDispose { }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        ProductDetailsScreenMainContent(
            productDetailsUiState = productDetailsUiState,
            onAction = {
                productDetailsViewModel.sendAction(it)
            },
            topAppBarState = topAppBarState,
            productDetailsListState = productDetailsListState
        )
        OnlineStoreSnackBarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        HandleUIStateChanges(
            productDetailsUiState = productDetailsUiState,
            snackBarHostState = snackBarHostState,
            backToPreviousScreen = backToPreviousScreen,
            gotoCartScreen = gotoCartScreen,
            productId = productId
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreenMainContent(
    productDetailsUiState: UiState<ProductDetailsUiModel>,
    onAction: (ProductDetailsAction) -> Unit,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    productDetailsListState: LazyListState = rememberLazyListState()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        ProductDetailsTopAppBarSection(
            topAppBarState = topAppBarState,
            productDetailsUiState = productDetailsUiState,
            onAction = onAction
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        ProductsDetailsScreenContent(
            productDetailsUiState = productDetailsUiState,
            modifier = Modifier
                .weight(1f),
            onAction = onAction,
            productDetailsListState = productDetailsListState
        )
    }
}

@Composable
private fun HandleUIStateChanges(
    productDetailsUiState: UiState<ProductDetailsUiModel>,
    snackBarHostState: SnackbarHostState,
    productId: String,
    backToPreviousScreen: () -> Unit,
    gotoCartScreen: () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    productDetailsViewModel: ProductDetailsViewModel =
        hiltViewModel<ProductDetailsViewModel,
                ProductDetailsViewModel.ProductDetailsViewModelFactory> { factory ->
            factory.create(productId = productId)
        }
) {
    val context = LocalContext.current
    val backToPreviousScreenState by rememberUpdatedState(backToPreviousScreen)
    val gotoCartScreenState by rememberUpdatedState(gotoCartScreen)

    LaunchedEffect(Unit) {
        productDetailsViewModel.uiEvent.collectLatest { event ->
            when (event) {
                is ProductDetailsEvent.BackToPreviousScreen -> {
                    backToPreviousScreenState()
                }

                is ProductDetailsEvent.GotoCartScreen -> {
                    gotoCartScreenState()
                }

                is ProductDetailsEvent.ShowMessage -> {
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
    if (productDetailsUiState is UiState.Loading) {
        ShowDashedProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true, device = Devices.PIXEL_XL)
private fun ProductDetailsScreenUIPreview() {
    ProductDetailsScreenMainContent(
        productDetailsUiState = UiState.Result(
            ProductDetailsUiModel(
                product = ProductItem(
                    name = "Swarowski",
                    price = 2000.0,
                    isWishListed = true,
                    isAddedToCart = false,
                    description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
                    image = "https://picsum.photos/200"
                )
            )
        ),
        onAction = {}
    )
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_XL)
private fun ProductDetailsScreenUIPreviewDark() {
    ProductDetailsScreenUIPreview()
}
