package com.panelsense.data.api

import com.google.gson.Gson
import com.panelsense.data.mapper.toWebsocketModel
import com.panelsense.data.model.WebsocketModel
import com.panelsense.data.repository.IncomingMessageProcessor
import com.panelsense.domain.model.ConnectionState
import com.panelsense.domain.model.entity.command.EntityCommand
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

class WebsocketConnectionProvider @Inject constructor(
    private val reconnectHelper: ReconnectHelper,
    private val okHttpClient: OkHttpClient,
    private val incomingMessageProcessor: IncomingMessageProcessor,
    private val gson: Gson
) {


    private var webSocket: WebSocket? = null
    private val websocketScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _connectionStateFlow: MutableStateFlow<ConnectionState> =
        MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionStateFlow: Flow<ConnectionState> = _connectionStateFlow
        .shareIn(websocketScope, SharingStarted.Eagerly, 1)

    object ConnectionOpen

    private var connectionFlow: MutableStateFlow<Result<ConnectionOpen>?>? = null


    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Timber.d("onOpen")
            _connectionStateFlow.value = ConnectionState.CONNECTED
            reconnectHelper.connectionOpen(webSocket)
            connectionFlow?.value = Result.success(ConnectionOpen)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Timber.d("onMessage: $text")
            runCatching {
                incomingMessageProcessor.processMessage(
                    gson.fromJson(
                        text,
                        WebsocketModel::class.java
                    )
                )
            }.onFailure(Timber::e)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            _connectionStateFlow.value = ConnectionState.DISCONNECTED
            reconnectHelper.connectionClosed()
            webSocket.cancel()
            Timber.d("onClosing: code: $code, reason: $reason")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            _connectionStateFlow.value = ConnectionState.DISCONNECTED
            webSocket.cancel()
            Timber.d("onClosed: code: $code, reason: $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            _connectionStateFlow.value = ConnectionState.DISCONNECTED
            Timber.e(t, "Websocket connection failure!")
            reconnectHelper.connectionClosed()
            webSocket.cancel()
            connectionFlow?.value = Result.failure(t)
        }
    }

    suspend fun connectToClient(serverIp: String, port: String): Result<ConnectionOpen> {
        Timber.d("Connecting to websocket")
        val request: Request = Request.Builder()
            .url("ws://$serverIp:$port")
            .build()
        connectionFlow = MutableStateFlow(null)
        webSocket = okHttpClient.newWebSocket(request, webSocketListener)
        reconnectHelper.initialize(request, webSocketListener) { webSocket = it }
        return connectionFlow!!.mapNotNull { it }.first()
    }

    fun sendMessage(message: Any) = websocketScope.launch {
        val jsonMessage = gson.toJson(message)
        Timber.d("Sending message: $jsonMessage")
        webSocket?.send(jsonMessage)
    }

    fun sendCommand(command: EntityCommand) {
        val jsonMessage = gson.toJson(command.toWebsocketModel(gson))
        Timber.d("Sending message: $jsonMessage")
        webSocket?.send(jsonMessage)
    }
}
