package com.panelsense.data.api

import com.panelsense.data.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber
import javax.inject.Inject

class ReconnectHelper @Inject constructor(private val okHttpClient: OkHttpClient) {

    private lateinit var request: Request
    private lateinit var callback: (WebSocket) -> Unit
    private lateinit var webSocketListener: WebSocketListener
    private var repeatScope: CoroutineScope? = null
    private var connectionClosedAccidentally: Boolean = false

    fun initialize(
        request: Request,
        webSocketListener: WebSocketListener,
        callback: (WebSocket) -> Unit
    ) {
        this.request = request
        this.callback = callback
        this.webSocketListener = webSocketListener
        connectionClosedAccidentally = false
    }

    fun connectionClosed() {
        startConnectingAttempt()
        connectionClosedAccidentally = true
    }

    fun connectionOpen(webSocket: WebSocket) {
        repeatScope?.cancel()
        if (connectionClosedAccidentally) {
            this.callback(webSocket)
        }
    }

    private fun startConnectingAttempt() {
        repeatScope?.cancel()
        repeatScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        repeatScope?.launch {
            while (true) {
                delay(RECONNECT_DELAY)
                Timber.d("Reconnecting to websocket")
                okHttpClient.newWebSocket(request, webSocketListener)
            }
        }
    }

    companion object {
        private val RECONNECT_DELAY = if (BuildConfig.DEBUG) 5_000L else 10_000L
    }
}
