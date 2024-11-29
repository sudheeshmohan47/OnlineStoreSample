package com.sample.productsmodule.presentation

import com.sample.onlinestore.commonmodule.domain.exception.NetworkException
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.productsmodule.domain.ProductsUseCase
import com.sample.onlinestore.productsmodule.domain.model.ProductItem
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingAction
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingEvent
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingUiModel
import com.sample.onlinestore.productsmodule.presentation.productslisting.ProductsListingViewModel
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

class ProductListingViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    public var mainCoroutineRule: MainCoroutineRule = MainCoroutineRule()

    private val dispatcher = StandardTestDispatcher()

    private lateinit var viewModel: ProductsListingViewModel
    private lateinit var productsUseCase: ProductsUseCase
    private lateinit var productsRepository: FakeProductRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        productsRepository = FakeProductRepository()
        productsUseCase = ProductsUseCase(productsRepository)
        viewModel = ProductsListingViewModel(
            productsUseCase = productsUseCase,
            initialScreenState = UiState.Loading(ProductsListingUiModel()),
            dispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `Ensure Default data for product listing is set correctly`() =
        runTest {
            val uiModel = viewModel.uiState.value.data ?: ProductsListingUiModel()
            assertFalse(uiModel.isSwipeRefreshing)
            assertFalse(uiModel.isInitialLoadingCompleted)
            assertTrue(uiModel.products.isEmpty())
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN data is loaded successfully first time, THEN then initialLoadingCompleted is set to true`() =
        runTest {

            // Trigger the action to refresh data
            viewModel.sendAction(ProductsListingAction.RefreshData)

            // Ensure that the UI state has changed and the data is loaded
            advanceUntilIdle()  // Ensure that background operations (like the IO tasks) are completed

            val initialLoadingCompleted = viewModel.uiState.value.data?.isInitialLoadingCompleted
            assertTrue(initialLoadingCompleted == true)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN screen is loaded THEN products data is loaded correctly when onResume`() = runTest {

        // Trigger the action to refresh data
        viewModel.sendAction(ProductsListingAction.RefreshData)

        // Ensure that the UI state has changed and the data is loaded
        advanceUntilIdle()  // Ensure that background operations (like the IO tasks) are completed

        val uiModel = viewModel.uiState.value.data ?: ProductsListingUiModel()
        val products = uiModel.products
        // Assert that the event is correct
        assertTrue(products.isNotEmpty())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `BEFORE fetching product uiState is Loading and AFTER fetching product uiState is Result `() =
        runTest {
            val uiStates = mutableListOf<UiState<ProductsListingUiModel>>()

            // Collect events in a separate coroutine
            val job = launch {
                viewModel.uiState.collectLatest { uiState ->
                    uiStates.add(uiState)
                }
            }
            // RefreshData action is sent during onResume lifecycle event
            viewModel.sendAction(ProductsListingAction.RefreshData)
            advanceUntilIdle()
            val products = viewModel.uiState.value.data?.products
            // Assert that the event is correct
            assertTrue(products?.isNotEmpty() == true)
            // Loading was showed before fetching
            assertTrue(uiStates[0] is UiState.Loading)
            // Result was shown afterwards
            assertTrue(uiStates[1] is UiState.Result)
            // Cancel the collector to avoid hanging
            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN item is clicked, THEN LoadProductDetailScreen event should be sent`() = runTest {
        val events = mutableListOf<ProductsListingEvent>()

        // Collect events in a separate coroutine
        val job = launch {
            viewModel.uiEvent.collectLatest { event ->
                events.add(event)
            }
        }
        viewModel.sendAction(ProductsListingAction.OnClickProduct(ProductItem()))
        advanceUntilIdle()
        // Assert that the event is correct
        assertTrue(events.last() is ProductsListingEvent.LoadProductDetailScreen)
        // Cancel the collector to avoid hanging
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN products fetching API throws Network error, THEN appropriate error should be updated in uiState`() =
        runTest {
            val events = mutableListOf<ProductsListingEvent>()
            // Collect events in a separate coroutine
            val job = launch {
                viewModel.uiEvent.collectLatest { event ->
                    events.add(event)
                }
            }
            productsRepository.exception = NetworkException()

            // RefreshData action is sent during onResume lifecycle event
            viewModel.sendAction(ProductsListingAction.RefreshData)
            advanceUntilIdle()

            assertTrue(events.last() is ProductsListingEvent.ShowMessage)
            val showMessage = events.last() as ProductsListingEvent.ShowMessage
            assertTrue(showMessage.message.messageResId > 0)

            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN OnClickFavorite is triggered THEN product should be added to favorites and appropriate event sent`() =
        runTest {
            val events = mutableListOf<ProductsListingEvent>()
            val testProduct = ProductItem(productId = "1")

            // Collect events in a separate coroutine
            val job = launch {
                viewModel.uiEvent.collectLatest { event ->
                    events.add(event)
                }
            }
            productsRepository.exception = null
            viewModel.sendAction(ProductsListingAction.RefreshData)
            advanceUntilIdle()

            viewModel.sendAction(ProductsListingAction.OnClickFavourite(testProduct))
            advanceUntilIdle()

            // Check if the product is added to favorites in the repository
            assertTrue(productsRepository.wishlist.contains(testProduct.productId))
            val wishListedProduct =
                viewModel.uiState.value.data?.products?.find { it.productId == testProduct.productId }
            assertTrue(wishListedProduct?.isWishListed == true)

            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN OnClickFavorite is triggered BUT API fails, THEN product status should revert`() =
        runTest {
            val events = mutableListOf<ProductsListingEvent>()
            val testProduct = ProductItem(productId = "1")

            // Collect events in a separate coroutine
            val job = launch {
                viewModel.uiEvent.collectLatest { event ->
                    events.add(event)
                }
            }

            productsRepository.exception = null
            viewModel.sendAction(ProductsListingAction.RefreshData)
            advanceUntilIdle()

            productsRepository.exception = NetworkException()
            viewModel.sendAction(ProductsListingAction.OnClickFavourite(testProduct))
            advanceUntilIdle()

            // Check if the product is added to favorites in the repository
            assertFalse(productsRepository.wishlist.contains(testProduct.productId))
            val wishListedProduct =
                viewModel.uiState.value.data?.products?.find { it.productId == testProduct.productId }
            assertTrue(wishListedProduct?.isWishListed == false)

            job.cancel()
        }

/*    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN OnClickFavorite is triggered, then it should optimistically update UI,  BUT API fails, THEN product status should revert`() =
        runTest {

            val uiStates = mutableListOf<UiState<ProductsListingUiModel>>()

            // Collect events in a separate coroutine
            val uiStateJob = launch {
                viewModel.uiState.collectLatest { uiState ->
                    uiStates.add(uiState)
                }
            }
            val events = mutableListOf<ProductsListingEvent>()
            val testProduct = ProductItem(productId = "1")

            // Collect events in a separate coroutine
            val job = launch {
                viewModel.uiEvent.collectLatest { event ->
                    events.add(event)
                }
            }

            // first fetch products
            productsRepository.exception = null
            viewModel.sendAction(ProductsListingAction.RefreshData)
            advanceUntilIdle()

            // update network exception
            productsRepository.exception = NetworkException()
            viewModel.sendAction(ProductsListingAction.OnClickFavourite(testProduct))
            advanceUntilIdle()

            assertFalse(productsRepository.wishlist.contains(testProduct.productId))

            val wishListedProduct =
                viewModel.uiState.value.data?.products?.find { it.productId == testProduct.productId } ?: ProductItem()
            assertTrue(events.last() is ProductsListingEvent.ShowMessage)
            assertFalse(wishListedProduct.isWishListed)

            val optimisticallyUpdatedProduct = uiStates[2].data?.products?.find { it.productId == testProduct.productId}
           // val updatedProductAfterApiFail = uiStates[3].data?.products?.find { it.productId == testProduct.productId}

            assertTrue(optimisticallyUpdatedProduct?.isWishListed == true)
            //assertTrue(updatedProductAfterApiFail?.isWishListed == false)

            job.cancel()
            uiStateJob.cancel()
        }*/

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN Cart Icon is clicked, THEN Load Cart screen event should be sent`() = runTest {
        val events = mutableListOf<ProductsListingEvent>()

        // Collect events in a separate coroutine
        val job = launch {
            viewModel.uiEvent.collectLatest { event ->
                events.add(event)
            }
        }
        viewModel.sendAction(ProductsListingAction.OnClickCart)
        advanceUntilIdle()
        // Assert that the event is correct
        assertTrue(events.last() is ProductsListingEvent.GotoCartScreen)
        // Cancel the collector to avoid hanging
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN swipe refresh is enabled, THEN swipe refresh status should be updated, and api is called`() =
        runTest {
            val uiStates = mutableListOf<UiState<ProductsListingUiModel>>()

            // Collect events in a separate coroutine
            val job = launch {
                viewModel.uiState.collectLatest { uiState ->
                    uiStates.add(uiState)
                }
            }
            // RefreshData action is sent during onResume lifecycle event
            viewModel.sendAction(ProductsListingAction.SetSwipeRefreshingStatus(true))
            advanceUntilIdle()
            val swipeRefreshingStatus = viewModel.uiState.value.data?.isSwipeRefreshing
            // Assert that the event is correct
            assertTrue(swipeRefreshingStatus == true)
            // Loading was showed before fetching
            assertTrue(uiStates[0] is UiState.Loading)
            // Result was shown afterwards
            assertTrue(uiStates[1] is UiState.Result)
            job.cancel()
        }
}