package com.panelsense.domain.interactor

import com.panelsense.domain.repository.ServerRepository
import javax.inject.Inject

class PanelSenseInteractor @Inject constructor(
    private val serverRepository: ServerRepository,
) {
    fun connectionState() = serverRepository.connectionState()

    fun configuration() = serverRepository.configuration()
}
