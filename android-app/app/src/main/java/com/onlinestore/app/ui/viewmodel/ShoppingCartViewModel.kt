package com.onlinestore.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.onlinestore.app.data.remote.ProductDto
import com.onlinestore.app.ui.CartState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    val cartState: CartState
) : ViewModel() {
    fun addToCart(p: ProductDto) = cartState.add(p, 1)
}
