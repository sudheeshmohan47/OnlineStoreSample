package com.sample.onlinestore.cartmodule.presentation.cart

import com.sample.onlinestore.cartmodule.domain.CartUseCase
import com.sample.onlinestore.commonmodule.domain.exception.DomainException
import com.sample.onlinestore.commonmodule.domain.exception.UnauthorizedException
import com.sample.onlinestore.commonmodule.domain.exception.mapErrorMessage
import com.sample.onlinestore.commonmodule.domain.model.Message
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Handles cart operations and facilitates UI communication.
 *
 * This class connects the domain layer (`CartUseCase`) with the UI, managing tasks like
 * fetching cart items, removing items, updating UI state, and handling errors gracefully.
 *
 * @param cartUseCase Handles cart data and operations at the domain layer.
 * @param viewModelScope Coroutine scope tied to the ViewModel lifecycle.
 * @param sendState Updates the UI state with new data or loading/error states.
 * @param sendEvent Dispatches events for user notifications or actions.
 */
class CartViewModelManager(
    private val cartUseCase: CartUseCase,
    private val viewModelScope: CoroutineScope,
    private val sendState: (UiState<CartUiModel>) -> Unit,
    private val sendEvent: (CartEvent) -> Unit
) {
    /**
     * Fetches cart data and updates the UI state.
     *
     * @param currentState Current state of the UI.
     */
    fun fetchCartData(currentState: UiState<CartUiModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendState(UiState.Loading(currentState.data))
                cartUseCase.getCartItems { isSuccess, domainResponse ->
                    if (isSuccess) {
                        domainResponse.data?.let {
                            sendState(
                                UiState.Result(
                                    currentState.data?.copy(
                                        cartItems = it
                                    )
                                )
                            )
                        }
                    }
                }
            } catch (exception: DomainException) {
                handleException(exception, currentState)
            }
        }
    }

    /**
     * Removes an item from the cart and updates the state.
     *
     * @param currentState Current state of the UI.
     * @param productId ID of the product to remove.
     */
    fun removeItemFromCart(
        currentState: UiState<CartUiModel>,
        productId: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            cartUseCase.removeFromCart(productId) { isSuccess ->
                val updatedCartItems = currentState.data?.cartItems?.filter {
                    it.productId != productId
                }.orEmpty()
                sendState(UiState.Result(currentState.data?.copy(cartItems = updatedCartItems)))
            }
        }
    }

    /**
     * Handles errors during cart operations.
     *
     * @param exception The exception to handle.
     * @param currentState Current UI state.
     */
    private fun handleException(
        exception: DomainException,
        currentState: UiState<CartUiModel>
    ) {

        when (exception) {
            is UnauthorizedException -> {
                sendState(UiState.Result(currentState.data))
                // sendEvent(ProductsListingEvent.EndUserSession)
            }

            else -> {
                val errorMessage = mapErrorMessage(exception)
                sendState(
                    UiState.Result(
                        currentState.data
                    )
                )
                sendEvent(
                    CartEvent.ShowMessage(
                        Message(
                            errorMessage.messageResId,
                            errorMessage.message
                        )
                    )
                )
            }
        }
    }
}
