package com.sample.onlinestore.commonmodule.data.model.api

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("error")
    val responseError: ResponseError?,
    @SerializedName("data")
    val data: T?
)
