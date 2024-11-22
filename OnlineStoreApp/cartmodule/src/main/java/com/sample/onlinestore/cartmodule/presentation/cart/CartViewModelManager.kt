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
 * ViewModel manager for handling product listing, wishlist actions, and UI state updates.
 * Manages fetching products, adding/removing from wishlist, and error handling.
 *
 * @param productsUseCase The use case for fetching product data and interacting with the wishlist.
 * @param viewModelScope The CoroutineScope for launching background tasks.
 * @param sendState A function to update the UI state with [UiState] containing [ProductsListingUiModel].
 */
class CartViewModelManager(
    private val cartUseCase: CartUseCase,
    private val viewModelScope: CoroutineScope,
    private val sendState: (UiState<CartUiModel>) -> Unit,
    private val sendEvent: (CartEvent) -> Unit
) {
    /**
     * Fetches the product data and updates the UI state.
     *
     * @param currentState The current state of the UI to be updated.
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
     * Handles exceptions thrown during the product fetching or wishlist operations.
     * Displays an appropriate error message based on the exception type.
     *
     * @param exception The caught exception to be handled.
     * @param currentState The current state of the UI.
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
