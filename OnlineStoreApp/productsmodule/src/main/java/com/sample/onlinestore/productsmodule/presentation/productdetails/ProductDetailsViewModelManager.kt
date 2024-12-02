package com.sample.onlinestore.productsmodule.presentation.productdetails

import com.sample.onlinestore.commonmodule.domain.exception.DomainException
import com.sample.onlinestore.commonmodule.domain.exception.UnauthorizedException
import com.sample.onlinestore.commonmodule.domain.exception.mapErrorMessage
import com.sample.onlinestore.commonmodule.domain.model.Message
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.productsmodule.R
import com.sample.onlinestore.productsmodule.domain.ProductsUseCase
import com.sample.onlinestore.productsmodule.domain.model.ProductItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Manages the business logic for the Product Details screen, including fetching product details,
 * handling wishlist operations, and cart operations. It provides a way to send updates to the UI
 * state and trigger UI events.
 *
 * @property productsUseCase Use case layer for handling product-related operations.
 * @property viewModelScope The CoroutineScope tied to the ViewModel lifecycle.
 * @property sendState Callback to update the UI state.
 * @property sendEvent Callback to send events to the UI.
 */
class ProductDetailsViewModelManager(
    private val productsUseCase: ProductsUseCase,
    private val viewModelScope: CoroutineScope,
    private val sendState: (UiState<ProductDetailsUiModel>) -> Unit,
    private val sendEvent: (ProductDetailsEvent) -> Unit,
    private val dispatcher: CoroutineDispatcher
) {

    /**
     * Fetches product details for a given product ID and updates the UI state.
     *
     * @param productId The ID of the product to fetch details for.
     * @param currentState The current UI state to maintain existing data during loading.
     */
    fun fetchProductDetails(productId: String, currentState: UiState<ProductDetailsUiModel>) {
        viewModelScope.launch(dispatcher) {
            try {
                sendState(UiState.Loading(currentState.data))
                productsUseCase.getProductDetail(productId = productId) { isSuccessFul, domainResponse ->
                    if (isSuccessFul) {
                        domainResponse.data?.let {
                            sendState(
                                UiState.Result(
                                    currentState.data?.copy(
                                        product = it
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
     * Adds or removes a product from the wishlist and updates the UI state optimistically.
     * Reverts the UI state if the operation fails.
     *
     * @param currentState The current UI state containing the product details.
     */
    fun addOrRemoveProductToWishList(
        currentState: UiState<ProductDetailsUiModel>
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                val originalProductItem = currentState.data?.product ?: ProductItem()
                val isAdding = !originalProductItem.isWishListed
                val updatedProduct = originalProductItem.copy(isWishListed = isAdding)
                val wishlistErrorMessageResId =
                    if (isAdding) com.sample.wishlistmodule.R.string.error_failed_to_add_wishlist
                    else com.sample.wishlistmodule.R.string.error_failed_to_remove_from_wishlist

                // Optimistically update UI
                val updatedProductItem = currentState.data?.copy(product = updatedProduct)
                sendState(UiState.Result(updatedProductItem))

                if (isAdding) {
                    productsUseCase.addToWishlist(originalProductItem.productId) { isSuccess ->
                        handleApiResponseStatus(
                            currentState,
                            isSuccess,
                            originalProductItem,
                            wishlistErrorMessageResId
                        )
                    }
                } else {
                    productsUseCase.removeFromWishlist(originalProductItem.productId) { isSuccess ->
                        handleApiResponseStatus(
                            currentState,
                            isSuccess,
                            originalProductItem,
                            wishlistErrorMessageResId
                        )
                    }
                }
            } catch (exception: DomainException) {
                handleException(exception, currentState)
            }
        }
    }

    /**
     * Adds a product to the cart and updates the UI state optimistically.
     * Reverts the UI state if the operation fails.
     *
     * @param currentState The current UI state containing the product details.
     */
    fun addProductToCart(
        currentState: UiState<ProductDetailsUiModel>
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                val originalProductItem = currentState.data?.product ?: ProductItem()
                val updatedProduct = originalProductItem.copy(isAddedToCart = true)

                // Optimistically update UI
                val updatedProductItem = currentState.data?.copy(product = updatedProduct)

                sendState(UiState.Result(updatedProductItem))
                productsUseCase.addToCart(originalProductItem.productId) { isSuccess ->
                    if (isSuccess) {
                        sendEvent(ProductDetailsEvent.ShowMessage(Message(R.string.product_added_to_cart)))
                    } else handleApiResponseStatus(
                        currentState, false, originalProductItem,
                        com.sample.onlinestore.cartmodule.R.string.error_adding_to_cart_failed
                    )
                }
            } catch (exception: DomainException) {
                handleException(exception, currentState)
            }
        }
    }

    /**
     * Handles the response status from an API call and reverts the UI state in case of failure.
     *
     * @param currentState The current UI state.
     * @param isSuccess Whether the API call was successful.
     * @param originalProduct The original product before optimistic updates.
     * @param messageResId Resource ID of the error message to display.
     */
    private fun handleApiResponseStatus(
        currentState: UiState<ProductDetailsUiModel>,
        isSuccess: Boolean,
        originalProduct: ProductItem,
        messageResId: Int
    ) {
        if (!isSuccess) {
            // if wishlist api fails revert back to original state
            sendState(
                UiState.Result(
                    currentState.data?.copy(
                        product = originalProduct
                    )
                )
            )
            sendEvent(ProductDetailsEvent.ShowMessage(Message(messageResId)))
        }
    }

    /**
     * Handles exceptions during product fetching or wishlist operations and updates the UI
     * state or sends an appropriate event.
     *
     * @param exception The caught exception to handle.
     * @param currentState The current UI state to maintain data integrity.
     */
    private fun handleException(
        exception: DomainException,
        currentState: UiState<ProductDetailsUiModel>
    ) {

        when (exception) {
            is UnauthorizedException -> {
                sendState(currentState)
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
                    ProductDetailsEvent.ShowMessage(
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
