@file:Suppress("unused")
package com.mzenskprokat.app.api

import com.mzenskprokat.app.BuildConfig
import com.mzenskprokat.app.models.ContactInfo
import com.mzenskprokat.app.models.OrderRequest
import com.mzenskprokat.app.models.Product
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

/**
 * API интерфейс для взаимодействия с сервером Мценскпрокат
 *
 * В текущей версии приложение использует статические данные из ProductRepository.
 * Этот файл — заготовка для будущей интеграции с backend API.
 */
interface MzenskProkatApiService {

    /** Получить список всех продуктов */
    @GET("api/products")
    suspend fun getProducts(): Response<List<Product>>

    /** Получить продукт по ID */
    @GET("api/products/{id}")
    suspend fun getProductById(@Path("id") productId: String): Response<Product>

    /** Получить продукты по категории */
    @GET("api/products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): Response<List<Product>>

    /** Поиск продуктов */
    @GET("api/products/search")
    suspend fun searchProducts(@Query("q") query: String): Response<List<Product>>

    /** Отправить заказ */
    @POST("api/orders")
    suspend fun submitOrder(@Body order: OrderRequest): Response<OrderResponse>

    /** Получить контактную информацию */
    @GET("api/contacts")
    suspend fun getContactInfo(): Response<ContactInfo>
}

/** Ответ сервера при отправке заказа */
data class OrderResponse(
    val success: Boolean,
    val message: String,
    val orderId: String? = null
)

/**
 * Singleton для создания API клиента
 *
 * Здесь общий OkHttp + Retrofit. Логи включены только в DEBUG.
 */
object ApiClient {
    // TODO: заменить на реальный URL
    private const val BASE_URL = "https://api.mzenskprokat.ru/"

    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
            builder.addInterceptor(logging)
        }

        builder.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: MzenskProkatApiService by lazy {
        retrofit.create(MzenskProkatApiService::class.java)
    }
}