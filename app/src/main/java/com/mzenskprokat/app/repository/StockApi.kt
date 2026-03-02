package com.mzenskprokat.app.repository

import com.squareup.moshi.JsonClass
import retrofit2.http.GET

@JsonClass(generateAdapter = true)
data class StockResponse(
    val updatedAt: String,
    val stock: Map<String, Int>
)

interface StockApi {
    @GET("exec") // важно: если в URL .../exec
    suspend fun getStock(): StockResponse
}