package com.sample.productsmodule.domain

import com.sample.onlinestore.commonmodule.domain.exception.DomainException
import com.sample.onlinestore.commonmodule.domain.exception.NetworkException
import com.sample.onlinestore.productsmodule.domain.ProductsUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProductUseCaseTest {
    private lateinit var productRepository: FakeProductRepository
    private lateinit var productUseCase: ProductsUseCase

    @Before
    fun setUp() {
        productRepository = FakeProductRepository()
        productUseCase = ProductsUseCase(productRepository)
    }

    @Test
    fun `getProducts returns list of products`() = runBlocking {
        productRepository.products = getFakeProducts()

        productUseCase.getProducts { success, response ->
            assertTrue(success)
            assertEquals(10, response.data?.size)
        }
    }

    @Test(expected = DomainException::class)
    fun `getProducts throws exception when exception is set`() = runBlocking {
        val domainException = NetworkException()
        productRepository.exception = domainException

        productUseCase.getProducts { _, _ -> }
    }

    @Test
    fun `getProductDetail returns correct product`() = runBlocking {
        val productId = "5"
        productRepository.products = getFakeProducts()

        productUseCase.getProductDetail(productId) { success, response ->
            assertTrue(success)
            assertNotNull(response.data)
            assertEquals(productId, response.data?.productId)
        }
    }

    @Test (expected = DomainException::class)
    fun `getProductDetail throws error for non-existent product`() = runBlocking {
        val productId = "100"
        productRepository.products = getFakeProducts()

        productUseCase.getProductDetail(productId) { success, response ->
            assertTrue(success)
            assertNull(response.data)
        }
    }

    @Test
    fun `addToWishlist adds product to wishlist`() = runBlocking {
        val productId = "1"
        productUseCase.addToWishlist(productId, onCompletion = {
            assertTrue(productRepository.wishlist.contains(productId))
        })

    }

    @Test(expected = DomainException::class)
    fun `addToWishlist throws exception when exception is set`() = runBlocking {
        val productId = "1"
        val domainException = NetworkException()
        productRepository.exception = domainException

        productUseCase.addToWishlist(productId, onCompletion = {})
    }

    @Test
    fun `removeFromWishlist removes product from wishlist`() = runBlocking {
        val productId = "1"
        productRepository.wishlist.add(productId)

        productUseCase.removeFromWishlist(productId) { success ->
            assertTrue(success)
            assertFalse(productRepository.wishlist.contains(productId))
        }
    }

    @Test(expected = DomainException::class)
    fun `removeFromWishlist throws exception when product is not added to wishlist`() = runBlocking {
        val productId = "1"
        productRepository.wishlist = mutableListOf()

        productUseCase.removeFromWishlist(productId) { success ->
            assertFalse(success)
        }
    }

    @Test
    fun `addProductToCart adds product to cart`() = runBlocking {
        val productId = "3"
        productUseCase.addToCart(productId, onCompletion = {
            assertTrue(productRepository.cart.contains(productId))
        })

    }
}
