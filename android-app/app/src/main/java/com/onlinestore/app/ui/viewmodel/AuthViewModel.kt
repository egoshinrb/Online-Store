package com.onlinestore.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onlinestore.app.data.local.TokenStore
import com.onlinestore.app.data.remote.LoginRequest
import com.onlinestore.app.data.remote.NotificationWebSocketClient
import com.onlinestore.app.data.remote.RegisterRequest
import com.onlinestore.app.data.remote.StoreApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val tokenStore: TokenStore,
    private val api: StoreApi,
    private val notificationWebSocket: NotificationWebSocketClient
) : ViewModel() {

    val loggedIn = tokenStore.accessToken
        .map { !it.isNullOrBlank() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun login(email: String, password: String, onResult: (Throwable?) -> Unit) {
        viewModelScope.launch {
            try {
                val t = api.login(LoginRequest(email, password))
                tokenStore.save(t.accessToken, t.refreshToken)
                onResult(null)
            } catch (e: Throwable) {
                onResult(e)
            }
        }
    }

    fun register(email: String, password: String, name: String, onResult: (Throwable?) -> Unit) {
        viewModelScope.launch {
            try {
                val t = api.register(RegisterRequest(email, password, name))
                tokenStore.save(t.accessToken, t.refreshToken)
                onResult(null)
            } catch (e: Throwable) {
                onResult(e)
            }
        }
    }

    fun logout() {
        notificationWebSocket.disconnect()
        viewModelScope.launch { tokenStore.clear() }
    }

    /** WebSocket при уже сохранённой сессии (холодный старт). */
    fun connectRealtimeIfLoggedIn() {
        viewModelScope.launch {
            if (tokenStore.getAccessToken() != null) {
                notificationWebSocket.connect { }
            }
        }
    }
}
