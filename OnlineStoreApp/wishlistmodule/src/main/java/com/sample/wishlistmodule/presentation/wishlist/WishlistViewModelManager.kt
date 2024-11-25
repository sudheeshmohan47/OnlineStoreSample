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
 * ViewModelManager for managing the wishlist.
 * It interacts with the domain layer (WishlistUseCase)
 * to perform these actions and updates the UI state accordingly.
 *
 * @param wishlistUseCase The use case for managing wishlist-related actions.
 * @param viewModelScope The CoroutineScope for launching background tasks.
 * @param sendState A lambda function to update the UI state.
 * @param sendEvent A lambda function to send UI events.
 */
class WishlistViewModelManager(
    private val wishlistUseCase: WishlistUseCase,
    private val viewModelScope: CoroutineScope,
    private val sendState: (UiState<WishlistUiModel>) -> Unit,
    private val sendEvent: (WishlistEvent) -> Unit
) {

    /**
     * Fetches the wishlist data from the use case and updates the UI state with the fetched data.
     * The current state is preserved while loading data.
     *
     * @param currentState The current UI state to preserve during the loading process.
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

    /**
     * Removes an item from the wishlist and updates the UI by filtering out the removed item.
     *
     * @param currentState The current UI state to reflect the changes.
     * @param productId The product ID of the item to be removed from the wishlist.
     */
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

    /**
     * Removes an item from the wishlist by calling the removeFromWishlist use case function.
     * If successful, the UI is updated by removing the item.
     *
     * @param currentState The current UI state to reflect the changes.
     * @param productId The product ID of the item to be removed from the wishlist.
     */
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

    /**
     * Moves an item from the wishlist to the cart and updates the UI state accordingly.
     *
     * @param currentState The current UI state to reflect the changes.
     * @param productId The product ID of the item to be moved to the cart.
     */
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
     * Handles exceptions that occur during wishlist operations. Based on the type of exception,
     * appropriate actions are taken (e.g., logging the error or ending the user session).
     *
     * @param exception The exception that occurred during the wishlist operation.
     * @param currentState The current UI state to reflect the changes based on the exception.
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
