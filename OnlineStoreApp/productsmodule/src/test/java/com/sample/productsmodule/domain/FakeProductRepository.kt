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

    override suspend fun getProducts(onCompletion: (Boolean, DomainResponse<List<ProductResponse>>) -> Unit) {
        exception?.let {
            throw it
        } ?: run {
            products = getFakeProducts()
            onCompletion(true, DomainResponse(data = products))
        }
    }

    override suspend fun getProductDetail(
        productId: String,
        onCompletion: (Boolean, DomainResponse<ProductResponse>) -> Unit
    ) {
        val products = getFakeProducts()
        exception?.let {
            throw it
        } ?: run {
            if(products.find { productId == it.id } != null){
                onCompletion(true, DomainResponse(data = products.find { it.id == productId }))
            } else throw NotFoundException()
        }
    }

    override suspend fun addToWishlist(productId: String) {
        exception?.let {
            throw it
        } ?: run {
            wishlist.add(productId)
        }
    }

    override suspend fun removeFromWishlist(productId: String, onCompletion: (Boolean) -> Unit) {
        exception?.let {
            throw it
        } ?: run {
            if (wishlist.contains(productId)) {
                wishlist.remove(productId)
                onCompletion(true)
            } else throw NotFoundException()
        }
    }

    override suspend fun addProductToCart(productId: String) {
        exception?.let {
            throw it
        } ?: run {
            cart.add(productId)
        }
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