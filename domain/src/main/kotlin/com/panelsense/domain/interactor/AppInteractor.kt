package com.panelsense.domain.interactor

import com.panelsense.domain.repository.UserDataRepository
import javax.inject.Inject

class AppInteractor @Inject constructor(private val userDataRepository: UserDataRepository) {

    suspend fun getDeviceName(): String? =
        userDataRepository.getServerConnectionData()?.panelSenseName
}
