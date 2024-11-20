package com.sample.onlinestore.productsmodule.presentation.productslisting

import androidx.lifecycle.viewModelScope
import com.sample.onlinestore.commonmodule.foundation.base.BaseViewModel
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.productsmodule.domain.ProductsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

val productsListingViewModelCreationCallback =
    { factory: ProductsListingViewModel.ProductsListingViewModelFactory ->
        factory.create()
    }

@HiltViewModel(assistedFactory = ProductsListingViewModel.ProductsListingViewModelFactory::class)
class ProductsListingViewModel @AssistedInject constructor(
    productsUseCase: ProductsUseCase,
    @Assisted initialScreenState: UiState<ProductsListingUiModel>
) : BaseViewModel<UiState<ProductsListingUiModel>, ProductsListingAction, ProductsListingEvent>(
    initialScreenState
) {

    // Manager class for managing viewmodel business logic
    private val productsListingViewModelManager = ProductListingViewModelManager(
        productsUseCase,
        viewModelScope,
        ::sendState,
    )

    @AssistedFactory
    interface ProductsListingViewModelFactory {
        fun create(
            initialScreenState: UiState<ProductsListingUiModel> = UiState.Result(
                ProductsListingUiModel()
            )
        ): ProductsListingViewModel
    }

    override fun reduce(
        currentState: UiState<ProductsListingUiModel>,
        action: ProductsListingAction
    ): UiState<ProductsListingUiModel> {
        return handleReducerAction(currentState = currentState, action = action)
    }

    private fun handleReducerAction(
        action: ProductsListingAction,
        currentState: UiState<ProductsListingUiModel>
    ): UiState<ProductsListingUiModel> {

        var returnState = currentState
        when (action) {

            is ProductsListingAction.SetSwipeRefreshingStatus -> {
                returnState = UiState.Result(
                    currentState.data?.copy(
                        isSwipeRefreshing = action.isRefreshing
                    )
                )
            }

            else -> Unit
        }
        return returnState
    }

    override fun runSideEffect(
        action: ProductsListingAction,
        currentState: UiState<ProductsListingUiModel>
    ) {
        when (action) {
            is ProductsListingAction.OnClickProduct -> {
                sendEvent(ProductsListingEvent.LoadProductDetailScreen(action.product.productId))
            }

            is ProductsListingAction.RefreshData -> {
                productsListingViewModelManager.fetchProductsData(uiState.value)
            }

            is ProductsListingAction.OnClickFavourite -> {
                productsListingViewModelManager.addOrRemoveProductToWishList(
                    uiState.value,
                    action.product
                )
            }

            else -> Unit
        }
    }
}
