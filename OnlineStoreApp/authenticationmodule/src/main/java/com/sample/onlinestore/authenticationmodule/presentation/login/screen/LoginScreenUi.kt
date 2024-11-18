package com.sample.onlinestore.authenticationmodule.presentation.login.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.sample.designsystem.components.OnlineStoreButton
import com.sample.designsystem.components.OnlineStoreOutlinedTextField
import com.sample.designsystem.foundation.OnlineStoreSpacing
import com.sample.designsystem.foundation.dp
import com.sample.onlinestore.authenticationmodule.R
import com.sample.onlinestore.authenticationmodule.presentation.login.LoginAction
import com.sample.onlinestore.authenticationmodule.presentation.login.LoginUiModel
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.commonmodule.utils.getStringFromId

@Composable
fun UserFields(
    loginUiState: UiState<LoginUiModel>,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current
) {
    Column(modifier = modifier) {
        loginUiState.data?.let { uiModel ->
            val usernameError = getStringFromId(context, uiModel.username?.errorMessageResId ?: 0)
            val passwordError = getStringFromId(context, uiModel.password?.errorMessageResId ?: 0)
            OnlineStoreOutlinedTextField(
                text = uiModel.username?.value ?: "",
                hint = stringResource(R.string.label_enter_username),
                label = stringResource(R.string.label_username),
                onValueChange = {
                    onAction(LoginAction.OnUsernameChanged(it))
                },
                isError = usernameError.isNotEmpty(),
                errorMessage = usernameError,
            )
            Spacer(modifier = Modifier.height(OnlineStoreSpacing.SMALL.dp()))
            OnlineStoreOutlinedTextField(
                text = uiModel.password?.value ?: "",
                hint = stringResource(R.string.label_enter_password),
                label = stringResource(R.string.label_password),
                onValueChange = {
                    onAction(LoginAction.OnPasswordChanged(it))
                },
                isError = passwordError.isNotEmpty(),
                errorMessage = passwordError,
                visualTransformation = PasswordVisualTransformation()
            )
        }
    }
}

@Composable
fun ContinueButton(
    loginUiState: UiState<LoginUiModel>,
    modifier: Modifier = Modifier,
    onAction: (LoginAction) -> Unit
) {
    val uiModel = loginUiState.data ?: return
    val username = uiModel.username?.value ?: ""
    val password = uiModel.password?.value ?: ""
    val enableContinueButton = username.isNotEmpty() && password.isNotEmpty()
    OnlineStoreButton(
        label = stringResource(R.string.label_login),
        enabled = enableContinueButton,
        buttonStyle = MaterialTheme.typography.titleMedium,
        onClick = { onAction(LoginAction.OnClickLogin) },
        modifier = modifier.fillMaxWidth(),
        isLoadingEnabled = loginUiState is UiState.Loading,
    )
}
