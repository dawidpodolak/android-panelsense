package com.panelsense.domain.interactor

import com.panelsense.domain.model.entity.state.EntityState
import com.panelsense.domain.repository.ServerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class PanelSenseInteractor @Inject constructor(
    private val serverRepository: ServerRepository,
) {
    fun connectionState() = serverRepository.connectionState()

    fun configuration() = serverRepository.configuration()
    fun listenOnEntityState(): Flow<EntityState> {
        return emptyFlow()
    }
}
