package com.onlinestore.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onlinestore.app.data.remote.ProductDto
import com.onlinestore.app.data.remote.StoreApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val api: StoreApi
) : ViewModel() {

    private val _items = MutableStateFlow<List<ProductDto>>(emptyList())
    val items: StateFlow<List<ProductDto>> = _items.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                _items.value = api.favorites()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
