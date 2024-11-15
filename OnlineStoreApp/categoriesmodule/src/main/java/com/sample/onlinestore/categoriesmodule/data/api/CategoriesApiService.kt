package com.sample.onlinestore.categoriesmodule.data.api

import retrofit2.Response
import retrofit2.http.GET

interface CategoriesApiService {
    @GET("/products/categories")
    suspend fun getProductCategories(): Response<List<String>>
}
