package com.onlinestore.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onlinestore.app.data.remote.AddressDto
import com.onlinestore.app.data.remote.AddressRequest
import com.onlinestore.app.data.remote.StoreApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val api: StoreApi
) : ViewModel() {

    private val _addresses = MutableStateFlow<List<AddressDto>>(emptyList())
    val addresses: StateFlow<List<AddressDto>> = _addresses.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                _addresses.value = api.addresses()
            } catch (_: Exception) {
            }
        }
    }

    fun add(label: String?, line: String, lat: Double?, lng: Double?, isDefault: Boolean) {
        viewModelScope.launch {
            api.createAddress(
                AddressRequest(label, line, lat, lng, isDefault)
            )
            refresh()
        }
    }
}
