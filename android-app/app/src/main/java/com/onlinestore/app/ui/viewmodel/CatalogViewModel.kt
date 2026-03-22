package com.onlinestore.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onlinestore.app.data.remote.CategoryDto
import com.onlinestore.app.data.remote.ProductDto
import com.onlinestore.app.data.remote.StoreApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val api: StoreApi
) : ViewModel() {

    private val _categories = MutableStateFlow<List<CategoryDto>>(emptyList())
    val categories: StateFlow<List<CategoryDto>> = _categories.asStateFlow()

    private val _products = MutableStateFlow<List<ProductDto>>(emptyList())
    val products: StateFlow<List<ProductDto>> = _products.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        refresh()
    }

    fun refresh(categoryId: Long? = null) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                _categories.value = api.categories()
                _products.value = api.products(categoryId = categoryId).items
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
