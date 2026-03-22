package com.onlinestore.app.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface StoreApi {

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): TokenResponse

    @POST("api/auth/register")
    suspend fun register(@Body body: RegisterRequest): TokenResponse

    @GET("api/me")
    suspend fun me(): UserSummary

    @GET("api/categories")
    suspend fun categories(): List<CategoryDto>

    @GET("api/products")
    suspend fun products(
        @Query("categoryId") categoryId: Long? = null,
        @Query("q") q: String? = null,
        @Query("minPrice") minPrice: java.math.BigDecimal? = null,
        @Query("maxPrice") maxPrice: java.math.BigDecimal? = null,
        @Query("brand") brand: String? = null,
        @Query("inStockOnly") inStockOnly: Boolean? = null
    ): ProductListResponse

    @GET("api/products/{id}")
    suspend fun product(@Path("id") id: Long): ProductDto

    @GET("api/favorites")
    suspend fun favorites(): List<ProductDto>

    @POST("api/favorites/{productId}")
    suspend fun addFavorite(@Path("productId") productId: Long)

    @DELETE("api/favorites/{productId}")
    suspend fun removeFavorite(@Path("productId") productId: Long)

    @GET("api/orders")
    suspend fun orders(): List<OrderDto>

    @POST("api/orders")
    suspend fun createOrder(@Body body: CreateOrderRequest): OrderDto

    @GET("api/addresses")
    suspend fun addresses(): List<AddressDto>

    @POST("api/addresses")
    suspend fun createAddress(@Body body: AddressRequest): AddressDto

    @POST("api/payments/confirm")
    suspend fun confirmPayment(@Body body: PaymentConfirmRequest): PaymentConfirmResponse

    @POST("api/subscriptions")
    suspend fun subscribePush(@Body body: FcmSubscriptionRequest)
}
