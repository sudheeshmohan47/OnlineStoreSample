package com.sample.onlinestore.productsmodule.data

import com.sample.onlinestore.cartmodule.data.model.CartRequest
import com.sample.onlinestore.cartmodule.domain.CartRepository
import com.sample.onlinestore.categoriesmodule.domain.CategoriesRepository
import com.sample.onlinestore.commonmodule.data.model.api.ErrorBody
import com.sample.onlinestore.commonmodule.data.model.product.ProductResponse
import com.sample.onlinestore.commonmodule.domain.exception.mapErrors
import com.sample.onlinestore.commonmodule.domain.exception.mapException
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import com.sample.onlinestore.commonmodule.utils.parseErrorBody
import com.sample.onlinestore.productsmodule.data.api.ProductsApiService
import com.sample.onlinestore.productsmodule.domain.ProductsRepository
import com.sample.wishlistmodule.domain.WishlistRepository
import javax.inject.Inject

/**
 * Implementation of [ProductsRepository] that manages product data and wishlist/cart integration.
 *
 * Responsibilities:
 * - Fetch products and product details from the API.
 * - Mark products as wishListed based on [WishlistRepository].
 * - Handle wishlist and cart operations.
 * - Map API errors and exceptions to domain-specific exceptions.
 */
@SuppressWarnings("TooGenericExceptionCaught")
class ProductsService @Inject constructor(
    private val productsApiService: ProductsApiService,
    private val wishlistRepository: WishlistRepository,
    private val categoriesRepository: CategoriesRepository,
    private val cartRepository: CartRepository
) : ProductsRepository {

    /**
     * Fetches the list of products from the API and marks them as wishListed if applicable.
     * Filters products by selected categories if any are chosen.
     */
    override suspend fun getProducts(): DomainResponse<List<ProductResponse>> {
        try {
            val response = productsApiService.getProducts()
            if (response.isSuccessful) {
                response.body()?.let { products ->
                    val wishListItems = wishlistRepository.getWishlistItemsIds()
                    val selectedCategories = categoriesRepository.getSelectedCategories()
                    val updatedProducts = updateProducts(products, wishListItems, selectedCategories)
                    return DomainResponse(data = updatedProducts)
                }
            }
            val errorBody: ErrorBody? = response.parseErrorBody()
            throw mapErrors(response.code(), errorBody?.responseError?.message)
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    /**
     * Fetches details of a specific product and marks it as wishListed or added to cart if applicable.
     *
     * @param productId The ID of the product to fetch.
     */
    override suspend fun getProductDetail(productId: String): DomainResponse<ProductResponse> {
        try {
            val response = productsApiService.getProductDetail(productId)
            if (response.isSuccessful) {
                response.body()?.let { product ->
                    val wishListItemIds = wishlistRepository.getWishlistItemsIds()
                    val cartItems = cartRepository.getCartItemsLocal()

                    val updatedProduct = product.copy(
                        isWishListed = wishListItemIds.contains(product.id),
                        isAddedToCart = cartItems.any { it.productId == product.id }
                    )

                    return DomainResponse(data = updatedProduct)
                }
            }
            val errorBody: ErrorBody? = response.parseErrorBody()
            throw mapErrors(response.code(), errorBody?.responseError?.message)
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    /**
     * Adds a product to the wishlist.
     *
     * @param productId The ID of the product to add.
     */
    override suspend fun addToWishlist(productId: String) {
        try {
            wishlistRepository.addToWishlist(productId)
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    /**
     * Removes a product from the wishlist.
     *
     * @param productId The ID of the product to remove.
     * @return True if removed successfully, false otherwise.
     */
    override suspend fun removeFromWishlist(productId: String): Boolean {
        try {
            return wishlistRepository.removeFromWishlist(productId)
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    /**
     * Adds a product to the cart with a default quantity of 1.
     *
     * @param productId The ID of the product to add.
     */
    override suspend fun addProductToCart(productId: String) {
        try {
            cartRepository.addToCart(CartRequest(productId = productId, quantity = 1))
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    /**
     * Updates products based on selected categories and wishlist items.
     *
     * @param products List of products from the API.
     * @param wishListItemIds List of product IDs that are wishListed.
     * @param selectedCategories List of selected categories to filter by.
     * @return List of updated products.
     */
    private fun updateProducts(
        products: List<ProductResponse>,
        wishListItemIds: List<String>,
        selectedCategories: List<String>
    ): List<ProductResponse> {
        return products
            .let { productList ->
                if (selectedCategories.isNotEmpty()) {
                    productList.filter { selectedCategories.contains(it.category) }
                } else productList
            }
            .map { product ->
                product.copy(isWishListed = wishListItemIds.contains(product.id))
            }
    }
}
