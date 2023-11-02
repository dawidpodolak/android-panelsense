package com.panelsense.domain.repository

import com.panelsense.domain.model.Configuration
import com.panelsense.domain.model.ConnectionState
import com.panelsense.domain.model.LoginSuccess
import com.panelsense.domain.model.ServerConnectionData
import com.panelsense.domain.model.entity.command.EntityCommand
import com.panelsense.domain.model.entity.state.EntityState
import kotlinx.coroutines.flow.Flow

interface ServerRepository {
    suspend fun login(serverConnectionData: ServerConnectionData): Result<LoginSuccess>

    fun configuration(): Flow<Configuration>

    fun connectionState(): Flow<ConnectionState>

    fun observerEntitiesState(): Flow<EntityState>

    suspend fun requestEntitiesState(delay: Boolean = false)
    fun sendCommand(command: EntityCommand)
}
