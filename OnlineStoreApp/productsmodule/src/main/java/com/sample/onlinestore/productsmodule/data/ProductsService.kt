package com.sample.onlinestore.productsmodule.data

import com.sample.onlinestore.commonmodule.data.model.api.ErrorBody
import com.sample.onlinestore.commonmodule.domain.exception.mapErrors
import com.sample.onlinestore.commonmodule.domain.exception.mapException
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import com.sample.onlinestore.commonmodule.utils.parseErrorBody
import com.sample.onlinestore.productsmodule.data.api.ProductsApiService
import com.sample.onlinestore.productsmodule.data.model.ProductResponse
import com.sample.onlinestore.productsmodule.domain.ProductsRepository
import com.sample.wishlistmodule.domain.WishlistRepository
import timber.log.Timber
import javax.inject.Inject

/**
 * Implementation of [ProductsRepository] for managing product data and wishlist integration.
 *
 * Responsibilities:
 * - Fetch products and their details from the API.
 * - Mark products as wishListed based on [WishlistRepository].
 * - Handle wishlist operations (add/remove).
 * - Map API errors and exceptions to domain-specific formats.
 *
 * Dependencies:
 * - [ProductsApiService] for product data.
 * - [WishlistRepository] for wishlist operations.
 */
@SuppressWarnings("TooGenericExceptionCaught")
class ProductsService @Inject constructor(
    private val productsApiService: ProductsApiService,
    private val wishlistRepository: WishlistRepository,
) : ProductsRepository {

    /**
     * Fetches the list of products from the API and marks them as wishListed if applicable.
     *
     * @param onCompletion A callback invoked with the success status and the [DomainResponse]
     * containing the list of products.
     * @throws Exception If an error occurs during the API call or wishlist operations.
     */
    override suspend fun getProducts(onCompletion: (Boolean, DomainResponse<List<ProductResponse>>) -> Unit) {
        try {
            val response = productsApiService.getProducts()
            if (response.isSuccessful) {
                response.body()?.let { products ->
                    val wishListItems = wishlistRepository.getWishlistItems()
                    Timber.d("WishListed Items: $wishListItems")
                    // Update product item's wishlist status
                    val updatedProducts = products.map { product ->
                        val isWishListed = wishListItems.any { it.productId == product.id }
                        product.copy(isWishListed = isWishListed)
                    }
                    onCompletion(true, DomainResponse(data = updatedProducts))
                    return@getProducts
                }
            }
            val errorBody: ErrorBody? = response.parseErrorBody()
            throw mapErrors(response.code(), errorBody?.responseError?.message)
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    /**
     * Fetches the details of a specific product from the API.
     *
     * @param productId The ID of the product to fetch details for.
     * @param onCompletion A callback invoked with the success status and the [DomainResponse]
     * containing the product details.
     * @throws Exception If an error occurs during the API call.
     */
    override suspend fun getProductDetail(
        productId: String,
        onCompletion: (Boolean, DomainResponse<ProductResponse>) -> Unit
    ) {
        try {
            val response = productsApiService.getProductDetail(productId)
            if (response.isSuccessful) {
                response.body()?.let { product ->
                    onCompletion(true, DomainResponse(data = product))
                    return@getProductDetail
                }
            }
            val errorBody: ErrorBody? = response.parseErrorBody()
            throw mapErrors(response.code(), errorBody?.responseError?.message)
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    /**
     * Adds a product to the wishlist using the [WishlistRepository].
     *
     * @param productId The ID of the product to add to the wishlist.
     */
    override suspend fun addToWishlist(productId: String) {
        wishlistRepository.addToWishlist(productId)
    }

    /**
     * Removes a product from the wishlist using the [WishlistRepository].
     *
     * @param productId The ID of the product to remove from the wishlist.
     * @param onCompletion A callback invoked with the success status of the operation.
     */
    override suspend fun removeFromWishlist(
        productId: String,
        onCompletion: (Boolean) -> Unit
    ) {
        wishlistRepository.removeFromWishlist(
            productId,
            onCompletion = {
                onCompletion(true)
            }
        )
    }
}
