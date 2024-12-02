package com.sample.productsmodule.presentation

import com.sample.onlinestore.commonmodule.domain.exception.DomainException
import com.sample.onlinestore.commonmodule.domain.exception.NetworkException
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.productsmodule.domain.ProductsUseCase
import com.sample.onlinestore.productsmodule.domain.model.ProductItem
import com.sample.onlinestore.productsmodule.presentation.productdetails.ProductDetailsAction
import com.sample.onlinestore.productsmodule.presentation.productdetails.ProductDetailsEvent
import com.sample.onlinestore.productsmodule.presentation.productdetails.ProductDetailsUiModel
import com.sample.onlinestore.productsmodule.presentation.productdetails.ProductDetailsViewModel
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingAction
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingEvent
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingUiModel
import com.sample.productsmodule.MainCoroutineRule
import com.sample.productsmodule.domain.FakeProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductDetailsViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    public var mainCoroutineRule: MainCoroutineRule = MainCoroutineRule()

    private val dispatcher = StandardTestDispatcher()

    private lateinit var viewModel: ProductDetailsViewModel
    private lateinit var productsUseCase: ProductsUseCase
    private lateinit var productsRepository: FakeProductRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        productsRepository = FakeProductRepository()
        productsUseCase = ProductsUseCase(productsRepository)
        viewModel = ProductDetailsViewModel(
            productsUseCase = productsUseCase,
            initialScreenState = UiState.Loading(ProductDetailsUiModel()),
            productId = "1",
            dispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `Ensure Default data for product listing is set correctly`() =
        runTest {
            val uiModel = viewModel.uiState.value.data ?: ProductDetailsUiModel()
            assertTrue(uiModel.product == ProductItem())

        }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN screen is loaded THEN products data is loaded correctly when onResume`() = runTest {

        // Trigger the action to refresh data
        viewModel.sendAction(ProductDetailsAction.RefreshData)

        // Ensure that the UI state has changed and the data is loaded
        advanceUntilIdle()  // Ensure that background operations (like the IO tasks) are completed

        val uiModel = viewModel.uiState.value.data ?: ProductDetailsUiModel()
        val product = uiModel.product
        // Assert that the event is correct
        assertTrue(product.productId.isNotEmpty())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `BEFORE fetching product uiState is Loading and AFTER fetching product uiState is Result `() =
        runTest {
            val uiStates = mutableListOf<UiState<ProductDetailsUiModel>>()

            // Collect events in a separate coroutine
            val job = launch {
                viewModel.uiState.collectLatest { uiState ->
                    uiStates.add(uiState)
                }
            }
            // RefreshData action is sent during onResume lifecycle event
            viewModel.sendAction(ProductDetailsAction.RefreshData)
            advanceUntilIdle()
            val product = viewModel.uiState.value.data?.product
            // Assert that the event is correct
            assertTrue(product?.productId?.isNotEmpty() == true)
            // Loading was showed before fetching
            assertTrue(uiStates[0] is UiState.Loading)
            // Result was shown afterwards
            assertTrue(uiStates[1] is UiState.Result)
            // Cancel the collector to avoid hanging
            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN products fetching API throws Network error, THEN appropriate error should be updated in uiState`() =
        runTest {
            val events = mutableListOf<ProductDetailsEvent>()
            // Collect events in a separate coroutine
            val job = launch {
                viewModel.uiEvent.collectLatest { event ->
                    events.add(event)
                }
            }
            productsRepository.exception = NetworkException()

            // RefreshData action is sent during onResume lifecycle event
            viewModel.sendAction(ProductDetailsAction.RefreshData)
            advanceUntilIdle()

            assertTrue(events.last() is ProductDetailsEvent.ShowMessage)
            val showMessage = events.last() as ProductDetailsEvent.ShowMessage
            assertTrue(showMessage.message.messageResId > 0)

            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN OnClickFavorite is triggered THEN product should be added to favorites and appropriate event sent`() =
        runTest {
            val events = mutableListOf<ProductDetailsEvent>()
            val testProduct = ProductItem(productId = "1")

            // Collect events in a separate coroutine
            val job = launch {
                viewModel.uiEvent.collectLatest { event ->
                    events.add(event)
                }
            }
            productsRepository.exception = null
            viewModel.sendAction(ProductDetailsAction.RefreshData)
            advanceUntilIdle()

            viewModel.sendAction(ProductDetailsAction.OnClickFavourite)
            advanceUntilIdle()

            // Check if the product is added to favorites in the repository
            assertTrue(productsRepository.wishlist.contains(testProduct.productId))
            val productItem =
                viewModel.uiState.value.data?.product
            assertTrue(productItem?.isWishListed == true)

            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN OnClickFavorite is triggered THEN product should be removed from favorites wishlisted and appropriate event sent`() =
        runTest {
            val events = mutableListOf<ProductDetailsEvent>()
            val testProduct = ProductItem(productId = "1")

            // Collect events in a separate coroutine
            val job = launch {
                viewModel.uiEvent.collectLatest { event ->
                    events.add(event)
                }
            }
            productsRepository.exception = null
            viewModel.sendAction(ProductDetailsAction.RefreshData)
            advanceUntilIdle()

            productsRepository.wishlist = mutableListOf()

            // Add to wishlist
            viewModel.sendAction(ProductDetailsAction.OnClickFavourite)
            advanceUntilIdle()

            // Check if the product is added to favorites in the repository
            assertTrue(productsRepository.wishlist.contains(testProduct.productId))

            // Add to wishlist
            viewModel.sendAction(ProductDetailsAction.OnClickFavourite)
            advanceUntilIdle()

            // Check if the product is added to favorites in the repository
            assertFalse(productsRepository.wishlist.contains(testProduct.productId))

            val productItem =
                viewModel.uiState.value.data?.product
            assertTrue(productItem?.isWishListed == false)

            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN OnClickFavorite is triggered BUT API fails, THEN product status should revert`() =
        runTest {
            val events = mutableListOf<ProductDetailsEvent>()
            val testProduct = ProductItem(productId = "1")

            // Collect events in a separate coroutine
            val job = launch {
                viewModel.uiEvent.collectLatest { event ->
                    events.add(event)
                }
            }

            productsRepository.exception = null
            productsRepository.wishlist = mutableListOf()

            viewModel.sendAction(ProductDetailsAction.RefreshData)
            advanceUntilIdle()

            productsRepository.exception = NetworkException()
            viewModel.sendAction(ProductDetailsAction.OnClickFavourite)
            advanceUntilIdle()

            // Check if the product is added to favorites in the repository
            assertFalse(productsRepository.wishlist.contains(testProduct.productId))
            val wishListedProduct =
                viewModel.uiState.value.data?.product
            assertTrue(wishListedProduct?.isWishListed == false)

            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN Add to Cart is triggered THEN product should be added to Cart and appropriate event sent`() =
        runTest {
            val events = mutableListOf<ProductDetailsEvent>()
            val testProduct = ProductItem(productId = "1")

            // Collect events in a separate coroutine
            val job = launch {
                viewModel.uiEvent.collectLatest { event ->
                    events.add(event)
                }
            }
            productsRepository.exception = null
            viewModel.sendAction(ProductDetailsAction.RefreshData)
            advanceUntilIdle()

            viewModel.sendAction(ProductDetailsAction.OnClickAddToCart)
            advanceUntilIdle()

            // Check if the product is added to favorites in the repository
            assertTrue(productsRepository.cart.contains(testProduct.productId))
            val productItem =
                viewModel.uiState.value.data?.product
            assertTrue(productItem?.isAddedToCart == true)

            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN Add to Cart is triggered BUT API fails, THEN product status should revert`() =
        runTest {
            val events = mutableListOf<ProductDetailsEvent>()
            val testProduct = ProductItem(productId = "1")

            // Collect events in a separate coroutine
            val job = launch {
                viewModel.uiEvent.collectLatest { event ->
                    events.add(event)
                }
            }

            productsRepository.exception = null
            productsRepository.cart = mutableListOf()

            viewModel.sendAction(ProductDetailsAction.RefreshData)
            advanceUntilIdle()

            productsRepository.exception = NetworkException()
            viewModel.sendAction(ProductDetailsAction.OnClickAddToCart)
            advanceUntilIdle()

            // Check if the product is not added to cart in the repository
            assertFalse(productsRepository.cart.contains(testProduct.productId))
            val productItem =
                viewModel.uiState.value.data?.product
            assertTrue(productItem?.isAddedToCart == false)

            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN Product is added to Cart, THEN triggering cart event should send GotoCart event`() =
        runTest {
            val events = mutableListOf<ProductDetailsEvent>()

            // Collect events in a separate coroutine
            val job = launch {
                viewModel.uiEvent.collectLatest { event ->
                    events.add(event)
                }
            }

            // Add item to cart
            viewModel.sendAction(ProductDetailsAction.OnClickAddToCart)
            advanceUntilIdle()

            // Click cart again
            viewModel.sendAction(ProductDetailsAction.OnClickAddToCart)
            advanceUntilIdle()

            assertTrue(events.last() is ProductDetailsEvent.GotoCartScreen)

            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN triggering OnClickBack action, THEN appropriate event should be sent`() =
        runTest {
            val events = mutableListOf<ProductDetailsEvent>()

            // Collect events in a separate coroutine
            val job = launch {
                viewModel.uiEvent.collectLatest { event ->
                    events.add(event)
                }
            }

            viewModel.sendAction(ProductDetailsAction.OnClickBackNavigation)
            advanceUntilIdle()

            assertTrue(events.last() is ProductDetailsEvent.BackToPreviousScreen)

            job.cancel()
        }
}