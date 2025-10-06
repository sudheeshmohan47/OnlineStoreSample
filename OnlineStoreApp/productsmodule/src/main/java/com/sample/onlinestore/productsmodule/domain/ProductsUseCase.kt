package com.sample.onlinestore.productsmodule.domain

import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import com.sample.onlinestore.productsmodule.domain.model.ProductItem
import javax.inject.Inject

/**
 * Use case for managing products, wishlist, and cart operations.
 *
 * Provides methods to fetch products, fetch product details, manage wishlist items,
 * and add products to the cart.
 *
 * Dependencies:
 * - [ProductsRepository] for product data and wishlist/cart management.
 */
class ProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {

    /**
     * Fetches all products, filters out invalid items, and maps them to [ProductItem].
     *
     * @return [DomainResponse] containing a list of valid [ProductItem] objects.
     */
    suspend fun getProducts(): DomainResponse<List<ProductItem>> {
        val productsResponse = productsRepository.getProducts()
        val products = productsResponse.data
            ?.filter { !it.id.isNullOrEmpty() }
            ?.map { productResponse ->
                ProductItem(
                    name = productResponse.title,
                    price = productResponse.price,
                    productId = productResponse.id!!,
                    category = productResponse.category,
                    description = productResponse.description,
                    image = productResponse.image,
                    isWishListed = productResponse.isWishListed
                )
            }
        return DomainResponse(data = products)
    }

    /**
     * Fetches detailed information for a specific product.
     *
     * @param productId The ID of the product to fetch.
     * @return [DomainResponse] containing [ProductItem] if found, or null otherwise.
     */
    suspend fun getProductDetail(productId: String): DomainResponse<ProductItem> {
        val productData = productsRepository.getProductDetail(productId).data

        val productDetail = productData?.id?.takeIf { it.isNotEmpty() }?.let { id ->
            ProductItem(
                name = productData.title.orEmpty(),
                price = productData.price ?: 0.0,
                productId = id,
                category = productData.category.orEmpty(),
                description = productData.description.orEmpty(),
                image = productData.image.orEmpty(),
                isWishListed = productData.isWishListed,
                isAddedToCart = productData.isAddedToCart
            )
        }

        return DomainResponse(data = productDetail)
    }

    /**
     * Adds a product to the wishlist.
     *
     * @param productId The ID of the product to add.
     */
    suspend fun addToWishlist(productId: String) {
        productsRepository.addToWishlist(productId)
    }

    /**
     * Adds a product to the cart.
     *
     * @param productId The ID of the product to add.
     * @return `true` if the operation was successful.
     */
    suspend fun addToCart(productId: String): Boolean {
        productsRepository.addProductToCart(productId)
        return true
    }

    /**
     * Removes a product from the wishlist.
     *
     * @param productId The ID of the product to remove.
     * @return `true` if the removal was successful.
     */
    suspend fun removeFromWishlist(productId: String): Boolean {
        return productsRepository.removeFromWishlist(productId)
    }
}
