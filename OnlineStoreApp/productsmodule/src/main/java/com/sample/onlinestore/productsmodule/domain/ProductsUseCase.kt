package com.sample.onlinestore.productsmodule.domain

import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import com.sample.onlinestore.productsmodule.domain.model.ProductItem
import javax.inject.Inject

/**
 * Use case for handling product-related operations and integrating wishlist functionality.
 *
 * Responsibilities:
 * - Fetch products and filter out invalid ones.
 * - Map API product data to [ProductItem] domain model.
 * - Manage product wishlist operations (add/remove).
 * - Provide product details with validation.
 *
 * Dependencies:
 * - [ProductsRepository] for product data and wishlist management.
 */
class ProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {

    suspend fun getProducts(onCompletion: (Boolean, DomainResponse<List<ProductItem>>) -> Unit) {
        productsRepository.getProducts { isSuccess, domainResponse ->
            if (isSuccess) {
                val products = domainResponse.data
                    ?.filter { productResponse ->
                        !productResponse.id.isNullOrEmpty() // Ensure id is not null or empty
                    }
                    ?.map { productResponse ->
                        ProductItem(
                            name = productResponse.title,
                            price = productResponse.price,
                            productId = productResponse.id!!, // Safe to use `!!` as we've filtered for non-null ids
                            category = productResponse.category,
                            description = productResponse.description,
                            image = productResponse.image,
                            isWishListed = productResponse.isWishListed
                        )
                    }
                onCompletion(true, DomainResponse(data = products))
            }
        }
    }

    suspend fun getProductDetail(
        productId: String,
        onCompletion: (Boolean, DomainResponse<ProductItem>) -> Unit
    ) {
        productsRepository.getProductDetail(productId) { isSuccess, domainResponse ->
            if (isSuccess) {
                val productDetail = domainResponse.data?.let {
                    if (!it.id.isNullOrEmpty()) { // Check if id is not null or empty
                        ProductItem(
                            name = it.title,
                            price = it.price,
                            productId = it.id,
                            category = it.category,
                            description = it.description,
                            image = it.image,
                            isWishListed = it.isWishListed,
                            isAddedToCart = it.isAddedToCart
                        )
                    } else {
                        null // Return null if id is null or empty
                    }
                }
                onCompletion(true, DomainResponse(data = productDetail))
            }
        }
    }

    suspend fun addToWishlist(productId: String, onCompletion: (Boolean) -> Unit) {
        productsRepository.addToWishlist(productId)
        onCompletion(true)
    }

    suspend fun addToCart(productId: String, onCompletion: (Boolean) -> Unit) {
        productsRepository.addProductToCart(productId)
        onCompletion(true)
    }

    suspend fun removeFromWishlist(
        productId: String,
        onCompletion: (Boolean) -> Unit
    ) {
        productsRepository.removeFromWishlist(
            productId,
            onCompletion = {
                onCompletion(true)
            }
        )
    }
}
