package com.onlinestore.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onlinestore.app.data.remote.CreateOrderRequest
import com.onlinestore.app.data.remote.OrderDto
import com.onlinestore.app.data.remote.OrderItemRequest
import com.onlinestore.app.data.remote.StoreApi
import com.onlinestore.app.ui.CartState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderFlowViewModel @Inject constructor(
    private val api: StoreApi,
    private val cartState: CartState
) : ViewModel() {

    private val _orders = MutableStateFlow<List<OrderDto>>(emptyList())
    val orders: StateFlow<List<OrderDto>> = _orders.asStateFlow()

    private val _lastError = MutableStateFlow<String?>(null)
    val lastError: StateFlow<String?> = _lastError.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            try {
                _orders.value = api.orders()
            } catch (e: Exception) {
                _lastError.value = e.message
            }
        }
    }

    fun checkoutCashOnDelivery(address: String) {
        viewModelScope.launch {
            try {
                val lines = cartState.lines.value.map {
                    OrderItemRequest(it.product.id, it.quantity)
                }
                if (lines.isEmpty()) return@launch
                api.createOrder(
                    CreateOrderRequest(
                        items = lines,
                        paymentMethod = "CASH_ON_DELIVERY",
                        deliveryAddressId = null,
                        addressSnapshot = address
                    )
                )
                cartState.clear()
                loadOrders()
            } catch (e: Exception) {
                _lastError.value = e.message
            }
        }
    }
}
