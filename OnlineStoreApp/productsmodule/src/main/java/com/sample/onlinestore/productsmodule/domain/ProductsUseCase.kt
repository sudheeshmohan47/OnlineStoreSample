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

    suspend fun getProducts(): DomainResponse<List<ProductItem>> {

        val productsResponse = productsRepository.getProducts()
        val products = productsResponse.data
            ?.filter { productResponse ->
                !productResponse.id.isNullOrEmpty() // Ensure id is not null or empty
            }?.map { productResponse ->
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
        return DomainResponse(data = products)
    }

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

    suspend fun addToWishlist(productId: String, onCompletion: (Boolean) -> Unit) {
        productsRepository.addToWishlist(productId)
        onCompletion(true)
    }

    suspend fun addToCart(productId: String): Boolean {
        productsRepository.addProductToCart(productId)
        return true
    }

    suspend fun removeFromWishlist(productId: String): Boolean {
        return productsRepository.removeFromWishlist(productId)
    }
}
