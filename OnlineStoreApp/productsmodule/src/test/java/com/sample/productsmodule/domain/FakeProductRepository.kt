package com.sample.productsmodule.domain

import com.sample.onlinestore.commonmodule.data.model.product.ProductResponse
import com.sample.onlinestore.commonmodule.domain.exception.DomainException
import com.sample.onlinestore.commonmodule.domain.exception.NotFoundException
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import com.sample.onlinestore.productsmodule.domain.ProductsRepository

class FakeProductRepository : ProductsRepository {
    var products: List<ProductResponse> = mutableListOf()
    var wishlist: MutableList<String> = mutableListOf()
    var cart: MutableList<String> = mutableListOf()
    var exception: DomainException? = null

    override suspend fun getProducts(): DomainResponse<List<ProductResponse>> {
        exception?.let { throw it }

        products = getFakeProducts()
        return DomainResponse(data = products)
    }

    override suspend fun getProductDetail(productId: String): DomainResponse<ProductResponse> {
        exception?.let { throw it }

        val products = getFakeProducts()
        val product = products.find { it.id == productId }

        return if (product != null) {
            DomainResponse(data = product)
        } else {
            throw NotFoundException()
        }
    }

    override suspend fun addToWishlist(productId: String) {
        exception?.let { throw it }
        wishlist.add(productId)
    }

    override suspend fun removeFromWishlist(productId: String): Boolean {
        exception?.let { throw it }

        return if (wishlist.contains(productId)) {
            wishlist.remove(productId)
            true
        } else {
            throw NotFoundException()
        }
    }

    override suspend fun addProductToCart(productId: String) {
        exception?.let { throw it }
        cart.add(productId)
    }
}

fun getFakeProducts(): List<ProductResponse> {
    val products = mutableListOf<ProductResponse>()
    for (i in 1..10) {
        products.add(
            ProductResponse(
                id = i.toString(),
                title = "Product $i",
                price = i * 10.0,
                category = "Category ${(i % 3) + 1}",
                description = "Description for Product $i",
                image = "https://example.com/product$i.jpg",
                isWishListed = (i % 2 == 0),
                isAddedToCart = (i % 3 == 0)
            )
        )
    }
    return products
}