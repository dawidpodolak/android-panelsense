package com.panelsense.data.repository

import android.util.Base64
import com.panelsense.core.AppDataProvider
import com.panelsense.core.VersionDataProvider
import com.panelsense.data.api.WebsocketConnectionProvider
import com.panelsense.data.mapper.toEntityState
import com.panelsense.data.model.AuthDataModel
import com.panelsense.data.model.AuthModelRequest
import com.panelsense.data.model.AuthResultModel
import com.panelsense.data.model.MessageType
import com.panelsense.data.model.RequestEntitiesStates
import com.panelsense.data.model.state.CoverState
import com.panelsense.data.model.state.LightState
import com.panelsense.data.model.state.SwitchState
import com.panelsense.data.model.state.WeatherState
import com.panelsense.domain.model.Configuration
import com.panelsense.domain.model.ConnectionState
import com.panelsense.domain.model.LoginSuccess
import com.panelsense.domain.model.ServerConnectionData
import com.panelsense.domain.model.entity.command.EntityCommand
import com.panelsense.domain.model.entity.state.EntityState
import com.panelsense.domain.repository.ServerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PanelSenseRepository @Inject constructor(
    private val connectionProvider: WebsocketConnectionProvider,
    private val versionDataProvider: VersionDataProvider,
    private val appDataProvider: AppDataProvider
) : ServerRepository {

    private var successServerConnectionData: ServerConnectionData? = null
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val entityStateFlow = MutableSharedFlow<EntityState>(replay = 1000)

    init {
        startListeningForEntityState()
    }

    @Suppress("MagicNumber")
    override suspend fun login(serverConnectionData: ServerConnectionData): Result<LoginSuccess> {
        connectionProvider.connectToClient(
            serverConnectionData.serverIPAddress,
            serverConnectionData.serverPort
        )
            .getOrNull() ?: return Result.failure(IllegalStateException("Connection failed"))
        return loginToPanelSenseAddon(serverConnectionData)
    }

    private suspend fun loginToPanelSenseAddon(serverConnectionData: ServerConnectionData): Result<LoginSuccess> {
        val authData = AuthDataModel(
            token = serverConnectionData.getToken(),
            versionName = versionDataProvider.versionName,
            versionCode = versionDataProvider.versionCode,
            name = serverConnectionData.panelSenseName,
            installationId = appDataProvider.installationId()
        )

        connectionProvider.sendMessage(AuthModelRequest(data = authData))

        return runCatching {
            val message =
                connectionProvider.listenForMessages<AuthResultModel>(MessageType.AUTH).first()
            if (message.authResult == AuthResultModel.Result.FAILURE) {
                throw IllegalStateException("Login failed")
            }
            LoginSuccess
        }
            .onSuccess { this.successServerConnectionData = serverConnectionData }
            .onFailure { Timber.e(it) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun connectionState(): Flow<ConnectionState> = connectionProvider.connectionStateFlow
        .flatMapLatest { connectionState ->
            if (connectionState == ConnectionState.CONNECTED && successServerConnectionData != null) {
                delay(RELOGIN_DELAY)
                loginToPanelSenseAddon(serverConnectionData = successServerConnectionData!!)
                requestEntitiesState(true)
            }
            flowOf(connectionState)
        }

    override fun configuration(): Flow<Configuration> =
        connectionProvider.listenForMessages<Configuration>(MessageType.CONFIGURATION)
            .shareIn(coroutineScope, SharingStarted.Eagerly, replay = 1)

    override suspend fun requestEntitiesState(delay: Boolean) {
        if (delay) {
            delay(REQUEST_STATE_DELAY)
        }
        connectionProvider.sendMessage(RequestEntitiesStates())
    }

    override fun sendCommand(command: EntityCommand) {
        connectionProvider.sendCommand(command)
    }

    override fun observerEntitiesState(): Flow<EntityState> {
        return entityStateFlow
    }

    private inline fun <reified T> listenForEntityState(): Flow<T> {
        val type =
            MessageType.values().firstOrNull { it.dataClass == T::class.java } ?: return emptyFlow()
        return connectionProvider.listenForMessages(type)
    }

    private fun startListeningForEntityState() {
        coroutineScope.launch {
            merge(
                listenForEntityState<LightState>().map { it.toEntityState() },
                listenForEntityState<CoverState>().map { it.toEntityState() },
                listenForEntityState<SwitchState>().map { it.toEntityState() },
                listenForEntityState<WeatherState>().map { it.toEntityState() }
            ).collect(entityStateFlow::emit)
        }
    }

    private fun ServerConnectionData.getToken(): String {
        val token = "$userName:$password"
        val data =
            Base64.encodeToString(token.toByteArray(Charset.defaultCharset()), Base64.DEFAULT)
        return data.replace("\n", "")
    }

    private companion object {
        const val REQUEST_STATE_DELAY = 1500L
        const val RELOGIN_DELAY = 1000L
    }
}
