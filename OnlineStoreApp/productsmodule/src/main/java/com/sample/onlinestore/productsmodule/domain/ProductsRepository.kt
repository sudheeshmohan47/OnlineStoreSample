package com.sample.onlinestore.productsmodule.domain

import com.sample.onlinestore.commonmodule.data.model.product.ProductResponse
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse

interface ProductsRepository {
    suspend fun getProducts(onCompletion: (Boolean, DomainResponse<List<ProductResponse>>) -> Unit)
    suspend fun getProductDetail(
        productId: String,
        onCompletion: (Boolean, DomainResponse<ProductResponse>) -> Unit
    )

    suspend fun addToWishlist(productId: String)
    suspend fun removeFromWishlist(productId: String, onCompletion: (Boolean) -> Unit)
    suspend fun addProductToCart(productId: String)
}
