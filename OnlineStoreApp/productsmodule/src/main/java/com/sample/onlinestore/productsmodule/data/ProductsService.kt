package com.sample.onlinestore.productsmodule.data

import com.sample.datastoragemodule.data.database.dao.WishlistDao
import com.sample.onlinestore.commonmodule.data.model.api.ErrorBody
import com.sample.onlinestore.commonmodule.domain.exception.mapErrors
import com.sample.onlinestore.commonmodule.domain.exception.mapException
import com.sample.onlinestore.commonmodule.domain.model.DomainResponse
import com.sample.onlinestore.commonmodule.utils.parseErrorBody
import com.sample.onlinestore.productsmodule.data.api.ProductsApiService
import com.sample.onlinestore.productsmodule.data.model.ProductResponse
import com.sample.onlinestore.productsmodule.domain.ProductsRepository
import timber.log.Timber
import javax.inject.Inject

@SuppressWarnings("TooGenericExceptionCaught")
class ProductsService @Inject constructor(
    private val productsApiService: ProductsApiService,
    private val wishlistDao: WishlistDao,
) : ProductsRepository {

    override suspend fun getProducts(onCompletion: (Boolean, DomainResponse<List<ProductResponse>>) -> Unit) {
        try {
            val response = productsApiService.getProducts()
            if (response.isSuccessful) {
                response.body()?.let { categories ->
                    onCompletion(true, DomainResponse(data = categories))
                    return@getProducts
                }
            }
            val errorBody: ErrorBody? = response.parseErrorBody()
            throw mapErrors(response.code(), errorBody?.responseError?.message)
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    override suspend fun getProductDetail(
        productId: String,
        onCompletion: (Boolean, DomainResponse<ProductResponse>) -> Unit
    ) {
        try {
            val response = productsApiService.getProductDetail(productId)
            if (response.isSuccessful) {
                response.body()?.let { product ->
                    onCompletion(true, DomainResponse(data = product))
                    return@getProductDetail
                }
            }
            val errorBody: ErrorBody? = response.parseErrorBody()
            throw mapErrors(response.code(), errorBody?.responseError?.message)
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    override suspend fun addOrRemoveItemToWishList(
        productId: String,
        isWishListed: Boolean,
        onCompletion: (Boolean) -> Unit
    ) {
        try {
            if (isWishListed) {
                wishlistDao.addToWishlist(productId)
            } else {
                wishlistDao.removeFromWishlist(productId)
            }
        } catch (e: Exception) {
            Timber.e(e)
            onCompletion(false)
        }


    }
}
