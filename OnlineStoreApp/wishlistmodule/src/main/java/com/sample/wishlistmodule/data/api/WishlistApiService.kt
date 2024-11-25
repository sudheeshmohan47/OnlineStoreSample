package com.sample.wishlistmodule.data.api

import com.sample.onlinestore.commonmodule.data.model.product.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface WishlistApiService {
    @GET("/products")
    suspend fun getProducts(): Response<List<ProductResponse>>
}
