package com.onlinestore.app.ui

import com.onlinestore.app.data.remote.ProductDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

data class CartLine(val product: ProductDto, val quantity: Int)

@Singleton
class CartState @Inject constructor() {
    private val _lines = MutableStateFlow<List<CartLine>>(emptyList())
    val lines: StateFlow<List<CartLine>> = _lines.asStateFlow()

    fun add(product: ProductDto, qty: Int = 1) {
        val cur = _lines.value.toMutableList()
        val i = cur.indexOfFirst { it.product.id == product.id }
        if (i >= 0) {
            val line = cur[i]
            cur[i] = line.copy(quantity = line.quantity + qty)
        } else {
            cur.add(CartLine(product, qty))
        }
        _lines.value = cur
    }

    fun remove(productId: Long) {
        _lines.value = _lines.value.filter { it.product.id != productId }
    }

    fun clear() {
        _lines.value = emptyList()
    }

    fun total(): BigDecimal = _lines.value.fold(BigDecimal.ZERO) { acc, line ->
        acc.add(line.product.price.multiply(BigDecimal.valueOf(line.quantity.toLong())))
    }
}
