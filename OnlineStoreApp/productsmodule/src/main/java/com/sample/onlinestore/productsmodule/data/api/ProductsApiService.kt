package com.sample.onlinestore.productsmodule.data.api

import com.sample.onlinestore.productsmodule.data.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductsApiService {
    @GET("/products")
    suspend fun getProducts(): Response<List<ProductResponse>>

    @GET("/products/{id}")
    suspend fun getProductDetail(@Path("id") productId: String): Response<ProductResponse>
}
