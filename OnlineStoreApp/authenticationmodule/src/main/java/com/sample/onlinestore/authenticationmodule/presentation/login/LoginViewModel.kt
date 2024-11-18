package com.sample.onlinestore.authenticationmodule.presentation.login

import androidx.lifecycle.viewModelScope
import com.sample.onlinestore.authenticationmodule.domain.LoginUseCase
import com.sample.onlinestore.commonmodule.domain.model.FieldObject
import com.sample.onlinestore.commonmodule.foundation.base.BaseViewModel
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

val loginViewModelCreationCallback = { factory: LoginViewModel.LoginViewModelFactory ->
    factory.create()
}

const val ErrorFieldTypeUsername = "username"
const val ErrorFieldTypePassword = "password"
const val PasswordMinLength = 6

@SuppressWarnings("MagicNumber")
@HiltViewModel(assistedFactory = LoginViewModel.LoginViewModelFactory::class)
class LoginViewModel @AssistedInject constructor(
    loginUseCase: LoginUseCase,
    @Assisted initialScreenState: UiState<LoginUiModel>
) : BaseViewModel<UiState<LoginUiModel>, LoginAction, LoginEvent>(initialScreenState) {

    @AssistedFactory
    interface LoginViewModelFactory {
        fun create(initialScreenState: UiState<LoginUiModel> = UiState.Result(LoginUiModel())): LoginViewModel
    }

    private val loginViewModelManager = LoginViewModelManager(
        loginUseCase = loginUseCase,
        viewModelScope = viewModelScope,
        sendState = { sendState(it) },
        sendEvent = { sendEvent(it) }
    )

    /**
     * This function is for handling various ui data changes in compose
     */
    override fun reduce(
        currentState: UiState<LoginUiModel>,
        action: LoginAction
    ): UiState<LoginUiModel> {
        return handleReducerAction(currentState = currentState, action = action)
    }

    private fun handleReducerAction(
        action: LoginAction,
        currentState: UiState<LoginUiModel>
    ): UiState<LoginUiModel> {

        var returnState = currentState
        when (action) {
            is LoginAction.OnUsernameChanged -> {
                returnState =
                    UiState.Result(currentState.data?.copy(username = FieldObject(action.username)))
            }

            is LoginAction.OnPasswordChanged -> {
                returnState =
                    UiState.Result(currentState.data?.copy(password = FieldObject(action.password)))
            }

            is LoginAction.OnClickLogin -> {
                // Handle continue to login action
                returnState = loginViewModelManager.handleLoginProcess(currentState)
            }
        }
        return returnState
    }

    /**
     * This function is used for handling events like navigate to next screen,
     * get results from backend and show something etc
     */
    override fun runSideEffect(action: LoginAction, currentState: UiState<LoginUiModel>) {
        // Handle Side effects here like navigate to next screen,
        // get results from backend and show something etc
    }
}
