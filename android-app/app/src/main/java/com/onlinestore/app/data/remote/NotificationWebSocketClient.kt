package com.onlinestore.app.data.remote

import com.onlinestore.app.BuildConfig
import com.onlinestore.app.data.local.TokenStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import javax.inject.Inject
import javax.inject.Singleton

/**
 * WebSocket-клиент для уведомлений с сервера (`/ws/notifications`).
 * Подключение: `access_token` в query (как на сервере).
 */
@Singleton
class NotificationWebSocketClient @Inject constructor(
    private val tokenStore: TokenStore,
    private val okHttpClient: OkHttpClient
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var socket: WebSocket? = null

    fun connect(onText: (String) -> Unit) {
        scope.launch {
            val token = tokenStore.getAccessToken() ?: return@launch
            val base = BuildConfig.WS_BASE_URL.trimEnd('/')
            val url = "$base/ws/notifications?access_token=$token"
            val request = Request.Builder().url(url).build()
            socket?.close(1000, null)
            socket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
                override fun onMessage(webSocket: WebSocket, text: String) {
                    onText(text)
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {}

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    t.printStackTrace()
                }
            })
        }
    }

    fun disconnect() {
        socket?.close(1000, "bye")
        socket = null
    }
}
