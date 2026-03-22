package com.onlinestore.app.data.remote

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)

data class TokenResponse(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("tokenType") val tokenType: String,
    @SerializedName("expiresInSeconds") val expiresInSeconds: Long
)

data class UserSummary(
    val id: Long,
    val email: String,
    val name: String
)

data class CategoryDto(
    val id: Long,
    val name: String,
    val parentId: Long?,
    val sortOrder: Int
)

data class ProductDto(
    val id: Long,
    val categoryId: Long,
    val name: String,
    val description: String?,
    val price: java.math.BigDecimal,
    val unit: String,
    val imageUrl: String?,
    val stock: Int,
    val brand: String?,
    val favorite: Boolean
)

data class ProductListResponse(
    val items: List<ProductDto>,
    val total: Int
)

data class OrderItemRequest(
    val productId: Long,
    val quantity: Int
)

data class CreateOrderRequest(
    val items: List<OrderItemRequest>,
    val paymentMethod: String,
    val deliveryAddressId: Long?,
    val addressSnapshot: String?
)

data class OrderItemDto(
    val productId: Long,
    val productName: String,
    val quantity: Int,
    val price: java.math.BigDecimal
)

data class OrderDto(
    val id: Long,
    val status: String,
    val total: java.math.BigDecimal,
    val addressSnapshot: String?,
    val deliveryAddressId: Long?,
    val paymentMethod: String,
    val paymentStatus: String,
    val createdAt: String,
    val items: List<OrderItemDto>
)

data class AddressRequest(
    val label: String?,
    val addressLine: String,
    val latitude: Double?,
    val longitude: Double?,
    val defaultAddress: Boolean
)

data class AddressDto(
    val id: Long,
    val label: String?,
    val addressLine: String,
    val latitude: Double?,
    val longitude: Double?,
    val defaultAddress: Boolean
)

data class PaymentConfirmRequest(
    val orderId: Long,
    val paymentToken: String,
    val provider: String? = "GOOGLE_PAY"
)

data class PaymentConfirmResponse(
    val success: Boolean,
    val message: String,
    val transactionId: String?
)

data class FcmSubscriptionRequest(
    val fcmToken: String,
    val deviceId: String?
)
