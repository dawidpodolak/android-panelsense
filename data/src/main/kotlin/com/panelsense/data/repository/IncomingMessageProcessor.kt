package com.panelsense.data.repository

import com.google.gson.Gson
import com.panelsense.data.model.AuthResultModel
import com.panelsense.data.model.MessageType
import com.panelsense.data.model.WebsocketModel
import com.panelsense.data.model.state.DataState
import com.panelsense.domain.model.Configuration
import com.panelsense.domain.model.entity.state.EntityState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IncomingMessageProcessor @Inject constructor(private val gson: Gson) {

    private val _authMessageFlow = MutableStateFlow<AuthResultModel?>(null)
    private val _configurationMessageFlow = MutableStateFlow<Configuration?>(null)

    val authFlow: Flow<AuthResultModel> = _authMessageFlow.filterNotNull()
    val configurationFlow: Flow<Configuration> = _configurationMessageFlow.filterNotNull()

    private val entityStateMap = mutableMapOf<String, MutableSharedFlow<EntityState>>()
    private val processMessageScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun processMessage(websocketModel: WebsocketModel) {

        val entityState = websocketModel.parseWebsocketToEntityState()
        val authResultModel = parseGeneralMessage<AuthResultModel>(websocketModel, MessageType.AUTH)
        val configurationModel =
            parseGeneralMessage<Configuration>(websocketModel, MessageType.CONFIGURATION)

        processMessageScope.launch {
            entityState?.let { getFlowForEntity(entityState.entityId).emit(entityState) }
            authResultModel?.let { _authMessageFlow.emit(authResultModel) }
            configurationModel?.let { _configurationMessageFlow.emit(configurationModel) }
        }
    }

    private inline fun <reified T> parseGeneralMessage(
        websocketModel: WebsocketModel,
        messageType: MessageType
    ): T? =
        runCatching {
            gson.fromJson(websocketModel.data, T::class.java)
                .takeIf { websocketModel.type == messageType }
        }.getOrNull()

    private fun WebsocketModel.parseWebsocketToEntityState(): EntityState? {
        val typeClass = when (type) {
            MessageType.CONFIGURATION -> return null
            MessageType.AUTH -> return null
            else -> type.dataClass
        }

        return (gson.fromJson(data, typeClass) as? DataState)?.toDomainState().also { entityState ->
            if (entityState == null) {
                Timber.w("Error parsing class $typeClass to domain state with $data ")
            }
        }
    }

    fun observeEntitiesState(entity: String): Flow<EntityState> = getFlowForEntity(entity)

    private fun getFlowForEntity(entity: String): MutableSharedFlow<EntityState> {
        val entityStateFlow = entityStateMap[entity]

        if (entityStateFlow != null) {
            return entityStateFlow
        }

        synchronized(this) {
            val newFlow = MutableSharedFlow<EntityState>(
                replay = 1,
                onBufferOverflow = BufferOverflow.DROP_OLDEST
            )
            entityStateMap[entity] = newFlow
            return newFlow
        }
    }
}
