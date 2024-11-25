package com.sample.wishlistmodule.presentation.wishlist

import com.sample.onlinestore.commonmodule.domain.exception.DomainException
import com.sample.onlinestore.commonmodule.domain.exception.UnauthorizedException
import com.sample.onlinestore.commonmodule.domain.exception.mapErrorMessage
import com.sample.onlinestore.commonmodule.domain.model.Message
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.wishlistmodule.domain.WishlistUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel manager for handling product listing, wishlist actions, and UI state updates.
 * Manages fetching products, adding/removing from wishlist, and error handling.
 *
 * @param wishlistUseCase The use case for fetching product data and interacting with the wishlist.
 * @param viewModelScope The CoroutineScope for launching background tasks.
 * @param sendState A function to update the UI state with [UiState] containing [ProductsListingUiModel].
 */
class WishlistViewModelManager(
    private val wishlistUseCase: WishlistUseCase,
    private val viewModelScope: CoroutineScope,
    private val sendState: (UiState<WishlistUiModel>) -> Unit,
    private val sendEvent: (WishlistEvent) -> Unit
) {
    /**
     * Fetches the product data and updates the UI state.
     *
     * @param currentState The current state of the UI to be updated.
     */
    fun fetchWishlistData(currentState: UiState<WishlistUiModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendState(UiState.Loading(currentState.data))
                wishlistUseCase.getWishlistItems { isSuccessFul, domainResponse ->
                    if (isSuccessFul) {
                        domainResponse.data?.let {
                            sendState(
                                UiState.Result(
                                    currentState.data?.copy(
                                        wishListedProducts = it
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

    private fun handleWishlistItemFromUi(
        currentState: UiState<WishlistUiModel>,
        productId: String
    ) {
        val updatedWishListedProducts = currentState.data?.wishListedProducts?.filter {
            it.productId != productId
        }.orEmpty()
        sendState(
            UiState.Result(
                currentState.data?.copy(
                    wishListedProducts = updatedWishListedProducts
                )
            )
        )
    }

    fun removeFromWishlist(
        currentState: UiState<WishlistUiModel>,
        productId: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            wishlistUseCase.removeFromWishlist(productId) { isSuccess ->
                if (isSuccess) {
                    handleWishlistItemFromUi(currentState, productId)
                }
            }
        }
    }

    fun moveToCart(
        currentState: UiState<WishlistUiModel>,
        productId: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            wishlistUseCase.moveItemToCart(productId) { isSuccess ->
                if (isSuccess) {
                    handleWishlistItemFromUi(currentState, productId)
                }
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
        currentState: UiState<WishlistUiModel>
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
                    WishlistEvent.ShowMessage(
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
