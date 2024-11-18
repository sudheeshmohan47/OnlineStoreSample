package com.carelo.android.presentation.authentication.login.screen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.designsystem.foundation.OnlineStoreSize
import com.sample.designsystem.foundation.OnlineStoreSnackBarHost
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp
import com.sample.designsystem.foundation.ui.OnlineStoreTheme
import com.sample.onlinestore.authenticationmodule.presentation.login.LoginAction
import com.sample.onlinestore.authenticationmodule.presentation.login.LoginEvent
import com.sample.onlinestore.authenticationmodule.presentation.login.LoginUiModel
import com.sample.onlinestore.authenticationmodule.presentation.login.LoginViewModel
import com.sample.onlinestore.authenticationmodule.presentation.login.loginViewModelCreationCallback
import com.sample.onlinestore.authenticationmodule.presentation.login.screen.ContinueButton
import com.sample.onlinestore.authenticationmodule.presentation.login.screen.UserFields
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.commonmodule.utils.handleErrorMessage
import com.sample.onlinestore.commonmodule.utils.keyboardVisibility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    gotoHomeScreen: () -> Unit,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel(creationCallback = loginViewModelCreationCallback)
) {
    val loginUiState: UiState<LoginUiModel> =
        loginViewModel.uiState.collectAsStateWithLifecycle().value
    val isKeyboardVisible by keyboardVisibility()
    val snackBarHostState = remember { SnackbarHostState() }
    val listState: LazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(isKeyboardVisible) {
        if (isKeyboardVisible) {
            // Scroll to the last item based on the current state of the list
            val lastItemIndex = listState.layoutInfo.totalItemsCount - 1
            if (lastItemIndex >= 0) {
                listState.animateScrollToItem(lastItemIndex)
            }
        }
    }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onPrimary)
                .align(Alignment.Center)
        ) {
            LoginScreenContents(
                loginUiState = loginUiState,
                onAction = { loginAction ->
                    loginViewModel.sendAction(loginAction)
                },
                listState = listState,
                context = context
            )

        }
        OnlineStoreSnackBarHost(
            modifier = Modifier.align(Alignment.BottomCenter),
            hostState = snackBarHostState,
        )
        HandleUiState(
            uiState = loginUiState,
            snackbarHostState = snackBarHostState,
            gotoHomeScreen = gotoHomeScreen,
            coroutineScope = coroutineScope
        )
    }
}

@Composable
fun HandleUiState(
    uiState: UiState<LoginUiModel>,
    gotoHomeScreen: () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState,
    loginViewModel: LoginViewModel = hiltViewModel(creationCallback = loginViewModelCreationCallback)
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val gotoHomeScreenState by rememberUpdatedState(gotoHomeScreen)

    when (uiState) {
        is UiState.Result -> {
            uiState.errorMessage?.let {
                handleErrorMessage(
                    context = context,
                    snackBarHostState = snackbarHostState,
                    coroutineScope = coroutineScope,
                    errorMessage = it
                )
            }
        }

        is UiState.Loading -> {
            // ShowDashedProgressIndicator()
        }

        is UiState.Error -> {}
    }

    LaunchedEffect(Unit) {
        loginViewModel.uiEvent.collectLatest { event ->
            when (event) {
                is LoginEvent.LoginSuccess -> {
                    keyboardController?.hide()
                    gotoHomeScreenState()
                }
            }
        }
    }
}

@Composable
fun LoginScreenContents(
    loginUiState: UiState<LoginUiModel>,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    context: Context = LocalContext.current
) {
    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .padding(OnlineStoreSpacing.EXTRA_LARGE.dp())
    ) {
        item { Spacer(modifier = Modifier.height(200.dp)) }
        item {
            UserFields(
                onAction = onAction,
                loginUiState = loginUiState,
                context = context
            )
        }
        item { Spacer(modifier = Modifier.height(OnlineStoreSpacing.MEDIUM.dp())) }
        item {
            ContinueButton(
                onAction = onAction,
                loginUiState = loginUiState
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLoginScreen() {
    OnlineStoreTheme {
        LoginScreen(
            gotoHomeScreen = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewLoginScreenDark() {
    PreviewLoginScreen()
}
