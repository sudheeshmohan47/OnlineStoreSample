package com.sample.onlinestore.authenticationmodule.presentation.login

import com.sample.onlinestore.authenticationmodule.R
import com.sample.onlinestore.authenticationmodule.domain.LoginUseCase
import com.sample.onlinestore.commonmodule.domain.exception.DomainException
import com.sample.onlinestore.commonmodule.domain.exception.mapErrorMessage
import com.sample.onlinestore.commonmodule.domain.model.Message
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Manages the login process, including input validation, handling login requests,
 * updating UI state, and sending events to trigger UI actions.
 *
 * @param loginUseCase Use case that handles the actual login operation.
 * @param viewModelScope Coroutine scope used to launch asynchronous login tasks.
 * @param sendState Callback function for updating the UI state to reflect changes in login
 *   status, such as loading, success, or error states.
 * @param sendEvent Callback function to send login-related events, such as signaling
 *   a successful login, for the UI to handle appropriately.
 */
class LoginViewModelManager(
    private val loginUseCase: LoginUseCase,
    private val viewModelScope: CoroutineScope,
    private val sendState: (UiState<LoginUiModel>) -> Unit,
    private val sendEvent: (LoginEvent) -> Unit
) {

    /**
     * Handles the login process by validating input fields and updating the UI state accordingly.
     * If there are no validation errors, it initiates the login process; otherwise, it updates
     * the UI state to reflect the validation errors.
     *
     * This function performs the following steps:
     * - Validates the login fields (e.g., username and password) for required conditions.
     * - If validation succeeds, sets the UI state to loading and calls `submitLogin` to proceed with login.
     * - If validation fails, updates the UI state to show specific error messages for each field.
     *
     * @param currentState The current UI state containing the login UI model.
     * @return The updated UI state after handling validation and initiating the login process if valid.
     */
    fun handleLoginProcess(currentState: UiState<LoginUiModel>): UiState<LoginUiModel> {
        var returnState = currentState
        currentState.data?.let { loginUiModel ->
            val errors = validateFields(loginUiModel)
            if (errors.isEmpty()) {
                // No validation errors, proceed with login
                returnState = UiState.Loading(loginUiModel)
                submitLogin(currentState)
            } else {
                // Validation errors found, update error states for fields
                val updatedUiState = updateErrorStates(loginUiModel, errors)
                // Update UI state with the updated LoginUiModel
                returnState = UiState.Result(updatedUiState)
            }
        }
        return returnState
    }

    /**
     * Updates error states for UI fields based on validation results.
     *
     * @param loginUiModel The current login UI model.
     * @param errors A map of field types to error message resource IDs.
     * @return The updated login UI model with error states.
     */
    private fun updateErrorStates(
        loginUiModel: LoginUiModel,
        errors: Map<String, Int>
    ): LoginUiModel {
        // Here we are updating error of phoneNumber field. We can update the error states of
        // other fields like this
        val usernameWithErrorState =
            loginUiModel.username?.copy(errorMessageResId = errors[ErrorFieldTypeUsername])
        val passwordWithErrorState =
            loginUiModel.password?.copy(errorMessageResId = errors[ErrorFieldTypePassword])

        // We can set multiple fields error state like this if we have more fields
        return loginUiModel.copy(
            username = usernameWithErrorState,
            password = passwordWithErrorState
        )
    }

    /**
     * Validates the username and password fields for errors.
     *
     * @param loginUiModel The current login UI model.
     * @return A map containing field types with corresponding error messages if validation fails.
     */
    private fun validateFields(loginUiModel: LoginUiModel): Map<String, Int> {
        val errorMessages = mutableMapOf<String, Int>()

        val username = loginUiModel.username?.value.orEmpty()
        val password = loginUiModel.password?.value.orEmpty()

        if (username.isEmpty())
            errorMessages[ErrorFieldTypeUsername] = R.string.error_empty_username

        if (password.isEmpty())
            errorMessages[ErrorFieldTypePassword] = R.string.error_empty_password

        if (password.length < PasswordMinLength)
            errorMessages[ErrorFieldTypePassword] = R.string.error_password_min_length

        return errorMessages
    }

    /**
     * Initiates the login process by calling the use case to log in with provided credentials.
     *
     * @param currentState The current state containing the login UI model.
     */
    private fun submitLogin(currentState: UiState<LoginUiModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val username = currentState.data?.username?.value.orEmpty()
                val password = currentState.data?.password?.value.orEmpty()

                val isLoginSuccess = loginUseCase.loginUser(
                    username = username,
                    password = password
                )
                if (isLoginSuccess) {
                    sendState(UiState.Result(currentState.data))
                    sendEvent(LoginEvent.LoginSuccess)
                }
            } catch (exception: DomainException) {
                handleException(exception, currentState)
            }
        }
    }

    /**
     * Handles exceptions occurred during login.
     * Maps the exception to an error message and updates the UI state with the error message.
     *
     * @param exception The exception occurred during login.
     * @param currentState The current state containing the login UI model.
     */
    private fun handleException(
        exception: DomainException,
        currentState: UiState<LoginUiModel>
    ) {
        val errorMessage = mapErrorMessage(exception)
        sendState(
            UiState.Result(
                currentState.data,
                Message(errorMessage.messageResId, errorMessage.message)
            )
        )
    }
}
