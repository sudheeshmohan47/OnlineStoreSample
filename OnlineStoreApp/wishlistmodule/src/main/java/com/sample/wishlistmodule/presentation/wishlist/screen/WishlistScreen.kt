package com.sample.wishlistmodule.presentation.wishlist.screen

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
import com.sample.designsystem.components.OnlineStoreSnackBarHost
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.commonmodule.utils.assistedHiltViewModel
import com.sample.onlinestore.commonmodule.utils.handleErrorMessage
import com.sample.wishlistmodule.presentation.wishlist.WishlistAction
import com.sample.wishlistmodule.presentation.wishlist.WishlistEvent
import com.sample.wishlistmodule.presentation.wishlist.WishlistUiModel
import com.sample.wishlistmodule.presentation.wishlist.WishlistViewModel
import com.sample.wishlistmodule.presentation.wishlist.wishlistViewModelCreationCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    loadProductDetailScreen: (String) -> Unit,
    gotoCartScreen: () -> Unit,
    modifier: Modifier = Modifier,
    wishlistViewModel: WishlistViewModel = assistedHiltViewModel(
        creationCallback = wishlistViewModelCreationCallback
    )
) {
    val wishlistUiState by wishlistViewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val topAppBarState = rememberTopAppBarState()
    val wishlistGridState: LazyGridState = rememberLazyGridState()

    LifecycleResumeEffect(Unit) {
        wishlistViewModel.sendAction(WishlistAction.RefreshData)
        onPauseOrDispose { }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        WishlistMainContent(
            wishlistUiState = wishlistUiState,
            onAction = {
                wishlistViewModel.sendAction(it)
            },
            topAppBarState = topAppBarState,
            wishlistGridState = wishlistGridState,
        )
        OnlineStoreSnackBarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        HandleUIStateChanges(
            wishlistUiState = wishlistUiState,
            snackBarHostState = snackBarHostState,
            loadProductDetailScreen = loadProductDetailScreen,
            gotoCartScreen = gotoCartScreen
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistMainContent(
    wishlistUiState: UiState<WishlistUiModel>,
    onAction: (WishlistAction) -> Unit,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    wishlistGridState: LazyGridState = rememberLazyGridState(),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        WishlistTopAppBarSection(
            topAppBarState = topAppBarState,
            onAction = onAction
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        WishlistScreenContent(
            wishlistUiState = wishlistUiState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = OnlineStoreSpacing.SMALL.dp()),
            onAction = onAction,
            wishlistGridState = wishlistGridState,
        )
    }
}

@Composable
private fun HandleUIStateChanges(
    wishlistUiState: UiState<WishlistUiModel>,
    snackBarHostState: SnackbarHostState,
    loadProductDetailScreen: (String) -> Unit,
    gotoCartScreen: () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    wishlistViewModel: WishlistViewModel = assistedHiltViewModel(
        creationCallback = wishlistViewModelCreationCallback
    )
) {
    val context = LocalContext.current
    val loadDetailScreenState by rememberUpdatedState(loadProductDetailScreen)
    val gotoCartScreenState by rememberUpdatedState(gotoCartScreen)

    LaunchedEffect(Unit) {
        wishlistViewModel.uiEvent.collectLatest { event ->
            when (event) {
                is WishlistEvent.LoadProductDetailScreen -> {
                    loadDetailScreenState(event.productId)
                }

                is WishlistEvent.GotoCartScreen -> {
                    gotoCartScreenState()
                }

                is WishlistEvent.ShowMessage -> {
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
    if (wishlistUiState is UiState.Loading) {
        ShowDashedProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true, device = Devices.PIXEL_XL)
private fun WishlistScreenUIPreview() {
    WishlistMainContent(
        wishlistUiState = UiState.Result(
            WishlistUiModel()
        ),
        onAction = {}
    )
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_XL)
private fun WishlistScreenUIPreviewDark() {
    WishlistScreenUIPreview()
}
