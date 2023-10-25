package com.panelsense.data.api

import com.google.gson.Gson
import com.panelsense.data.model.WebsocketModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber
import javax.inject.Inject

class WebsocketConnectionProvider @Inject constructor(private val gson: Gson) {

    private val messageFlow: MutableStateFlow<WebsocketModel?> = MutableStateFlow(null)

    object ConnectionOpen

    private var connectionFlow: MutableStateFlow<Result<ConnectionOpen>?>? = null
    private val okHttpClient = OkHttpClient.Builder()
        .build()

    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Timber.d("onOpen")
            connectionFlow?.value = Result.success(ConnectionOpen)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Timber.d("onMessage: $text")
            runCatching {
                messageFlow.value = gson.fromJson(text, WebsocketModel::class.java)
            }.onFailure(Timber::e)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Timber.d("onClosing: code: $code, reason: $reason")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Timber.d("onClosed: code: $code, reason: $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Timber.e(t)
            connectionFlow?.value = Result.failure(t)
        }
    }

    private var webSocket: WebSocket? = null
    private val websocketScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    suspend fun connectToClient(serverIp: String, port: String): Result<ConnectionOpen> {
        val request: Request = Request.Builder()
            .url("ws://$serverIp:$port")
            .build()
        connectionFlow = MutableStateFlow<Result<ConnectionOpen>?>(null)
        webSocket = okHttpClient.newWebSocket(request, webSocketListener)

        return connectionFlow!!.mapNotNull { it }.first()
    }

    fun listenForMessages(): Flow<WebsocketModel> = messageFlow
        .mapNotNull { it }
        .shareIn(websocketScope, SharingStarted.Eagerly, 1)

    fun sendMessage(message: Any) = websocketScope.launch {
        webSocket?.send(gson.toJson(message))
    }
}
