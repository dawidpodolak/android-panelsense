package com.panelsense.domain.interactor

import com.panelsense.domain.model.entity.command.EntityCommand
import com.panelsense.domain.model.entity.state.EntityState
import com.panelsense.domain.repository.ServerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PanelSenseInteractor @Inject constructor(
    private val serverRepository: ServerRepository,
) {
    fun connectionState() = serverRepository.connectionState()

    fun configuration() = serverRepository.configuration()

    fun observeEntityState(entityId: String): Flow<EntityState> {
        return serverRepository.observerEntitiesState(entityId)
    }

    fun sendCommand(command: EntityCommand) {
        serverRepository.sendCommand(command)
    }
}
