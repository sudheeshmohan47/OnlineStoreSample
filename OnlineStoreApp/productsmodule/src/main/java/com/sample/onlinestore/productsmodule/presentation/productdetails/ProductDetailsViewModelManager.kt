package com.sample.onlinestore.productsmodule.presentation.productdetails

import com.sample.onlinestore.commonmodule.domain.exception.DomainException
import com.sample.onlinestore.commonmodule.domain.exception.UnauthorizedException
import com.sample.onlinestore.commonmodule.domain.exception.mapErrorMessage
import com.sample.onlinestore.commonmodule.domain.model.Message
import com.sample.onlinestore.commonmodule.foundation.base.Event
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.productsmodule.domain.ProductsUseCase
import com.sample.onlinestore.productsmodule.domain.model.ProductItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailsViewModelManager(
    private val productsUseCase: ProductsUseCase,
    private val viewModelScope: CoroutineScope,
    private val sendState: (UiState<ProductDetailsUiModel>) -> Unit,
    private val sendEvent: (ProductDetailsEvent) -> Unit
) {

    fun fetchProductDetails(productId: String, currentState: UiState<ProductDetailsUiModel>) {
        viewModelScope.launch(Dispatchers.IO) {
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
     * Adds or removes a product from the wishlist and updates the UI optimistically.
     *
     * @param currentState The current UI state that contains the list of products.
     */
    fun addOrRemoveProductToWishList(
        currentState: UiState<ProductDetailsUiModel>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
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
        }
    }

    /**
     * Adds or removes a product from the wishlist and updates the UI optimistically.
     *
     * @param currentState The current UI state that contains the list of products.
     */
    fun addProductToCart(
        currentState: UiState<ProductDetailsUiModel>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val originalProductItem = currentState.data?.product ?: ProductItem()
            val updatedProduct = originalProductItem.copy(isAddedToCart = true)

            // Optimistically update UI
            val updatedProductItem = currentState.data?.copy(product = updatedProduct)

            sendState(UiState.Result(updatedProductItem))
            productsUseCase.addToCart(originalProductItem.productId) { isSuccess ->
                handleApiResponseStatus(
                    currentState, isSuccess, originalProductItem,
                    com.sample.onlinestore.cartmodule.R.string.error_adding_to_cart_failed
                )
            }
        }
    }

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
                    ),
                    Message(messageResId)
                )
            )
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
                    ProductDetailsEvent.ShowMessage(Message(errorMessage.messageResId, errorMessage.message))
                )
            }
        }
    }
}
