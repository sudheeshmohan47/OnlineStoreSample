package com.sample.onlinestore.productsmodule.data

import com.sample.datastoragemodule.data.database.model.Wishlist
import com.sample.onlinestore.categoriesmodule.domain.CategoriesRepository
import com.sample.onlinestore.commonmodule.data.model.api.ErrorBody
import com.sample.onlinestore.commonmodule.domain.exception.mapErrors
import com.sample.onlinestore.commonmodule.domain.exception.mapException
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import com.sample.onlinestore.commonmodule.utils.parseErrorBody
import com.sample.onlinestore.productsmodule.data.api.ProductsApiService
import com.sample.onlinestore.productsmodule.data.model.ProductResponse
import com.sample.onlinestore.productsmodule.domain.ProductsRepository
import com.sample.wishlistmodule.domain.WishlistRepository
import com.sample.wishlistmodule.domain.model.WishlistItem
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
    private val categoriesRepository: CategoriesRepository,
) : ProductsRepository {

    /**
     * Fetches the list of products from the API and marks them as wishListed if applicable.
     *
     * @param onCompletion A callback invoked with the success status and the [DomainResponse]
     * containing the list of products.
     * @throws Exception If an error occurs during the API call or wishlist operations.
     */
    override suspend fun getProducts(
        onCompletion: (Boolean, DomainResponse<List<ProductResponse>>) -> Unit
    ) {
        try {
            val response = productsApiService.getProducts()
            if (response.isSuccessful) {
                response.body()?.let { products ->
                    val wishListItems = wishlistRepository.getWishlistItems()
                    val selectedCategories = categoriesRepository.getSelectedCategories()

                    // Call to update products based on wishList and selectedCategories
                    val updatedProducts = updateProducts(products, wishListItems, selectedCategories)

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

    /**
     * Update products based on the selected categories and wishlist items.
     */
    private fun updateProducts(
        products: List<ProductResponse>,
        wishListItems: List<Wishlist>,
        selectedCategories: List<String>
    ): List<ProductResponse> {
        return products
            .let { productList ->
                // Filter products based on selected categories if any
                if (selectedCategories.isNotEmpty()) {
                    productList.filter { product -> selectedCategories.contains(product.category) }
                } else {
                    productList
                }
            }
            .map { product ->
                // Mark products as wishListed if they exist in the wishlist
                val isWishListed = wishListItems.any { it.productId == product.id }
                product.copy(isWishListed = isWishListed)
            }
    }
}
