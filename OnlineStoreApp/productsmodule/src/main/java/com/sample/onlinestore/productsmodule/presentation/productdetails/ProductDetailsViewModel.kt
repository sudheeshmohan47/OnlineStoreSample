package com.sample.onlinestore.productsmodule.presentation.productdetails

import androidx.lifecycle.viewModelScope
import com.sample.onlinestore.commonmodule.foundation.base.BaseViewModel
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.productsmodule.domain.ProductsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

val productDetailsViewModelCreationCallback =
    { factory: ProductDetailsViewModel.ProductDetailsViewModelFactory ->
        factory.create()
    }

@HiltViewModel(assistedFactory = ProductDetailsViewModel.ProductDetailsViewModelFactory::class)
class ProductDetailsViewModel @AssistedInject constructor(
    productsUseCase: ProductsUseCase,
    @Assisted private val productId: String,
    @Assisted initialScreenState: UiState<ProductDetailsUiModel>
) : BaseViewModel<UiState<ProductDetailsUiModel>, ProductDetailsAction, ProductDetailsEvent>(
    initialScreenState
) {

    // Manager class for managing viewmodel business logic
    private val productDetailsViewModelManager = ProductDetailsViewModelManager(
        productsUseCase,
        viewModelScope,
        ::sendState,
    )

    @AssistedFactory
    interface ProductDetailsViewModelFactory {
        fun create(
            initialScreenState: UiState<ProductDetailsUiModel> = UiState.Result(
                ProductDetailsUiModel()
            )
        ): ProductDetailsViewModel
    }

    override fun reduce(
        currentState: UiState<ProductDetailsUiModel>,
        action: ProductDetailsAction
    ): UiState<ProductDetailsUiModel> {
        return handleReducerAction(currentState = currentState, action = action)
    }

    private fun handleReducerAction(
        action: ProductDetailsAction,
        currentState: UiState<ProductDetailsUiModel>
    ): UiState<ProductDetailsUiModel> {
        return currentState
    }

    override fun runSideEffect(
        action: ProductDetailsAction,
        currentState: UiState<ProductDetailsUiModel>
    ) {
        when (action) {
            is ProductDetailsAction.RefreshData -> {
                productDetailsViewModelManager.fetchProductDetails(productId, uiState.value)
            }

            is ProductDetailsAction.OnClickFavourite -> {
                productDetailsViewModelManager.addOrRemoveProductToWishList(uiState.value)
            }

            is ProductDetailsAction.OnClickBackNavigation -> {
                sendEvent(ProductDetailsEvent.BackToPreviousScreen)
            }

            is ProductDetailsAction.OnClickAddToCart -> {
                val isItemAddedToCart = uiState.value.data?.product?.isAddedToCart ?: false
                if (isItemAddedToCart) {
                    sendEvent(ProductDetailsEvent.GotoCartScreen)
                }else {
                    productDetailsViewModelManager.addProductToCart(uiState.value)
                }
            }
        }
    }
}
