package com.sample.onlinestore.productsmodule.domain

import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import com.sample.onlinestore.productsmodule.domain.model.ProductItem
import javax.inject.Inject

class ProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository,
) {

    suspend fun getProducts(onCompletion: (Boolean, DomainResponse<List<ProductItem>>) -> Unit) {
        productsRepository.getProducts { isSuccess, domainResponse ->
            if (isSuccess) {
                val products = domainResponse.data?.map { productResponse ->
                    ProductItem(
                        id = productResponse.id,
                        name = productResponse.name,
                        price = productResponse.price,
                        productId = productResponse.productId,
                        category = productResponse.category,
                        description = productResponse.description,
                        image = productResponse.image
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
                    ProductItem(
                        id = it.id,
                        name = it.name,
                        price = it.price,
                        productId = it.productId,
                        category = it.category,
                        description = it.description,
                        image = it.image
                    )
                }
                onCompletion(true, DomainResponse(data = productDetail))
            }
        }
    }
}
