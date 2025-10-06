package com.sample.onlinestore.productsmodule.domain

import com.sample.onlinestore.commonmodule.data.model.product.ProductResponse
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse

interface ProductsRepository {
    suspend fun getProducts(): DomainResponse<List<ProductResponse>>
    suspend fun getProductDetail(productId: String): DomainResponse<ProductResponse>
    suspend fun addToWishlist(productId: String)
    suspend fun removeFromWishlist(productId: String): Boolean
    suspend fun addProductToCart(productId: String)
}
