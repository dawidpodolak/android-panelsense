package com.panelsense.domain.interactor

import com.panelsense.domain.model.LoginSuccess
import com.panelsense.domain.model.ServerConnectionData
import com.panelsense.domain.repository.ServerRepository
import com.panelsense.domain.repository.UserDataRepository
import javax.inject.Inject

class LoginInteractor @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val serverRepository: ServerRepository
) {
    suspend fun isUserLoggedIn(): Boolean = userDataRepository.isUserLoggedIn()

    suspend fun login(serverConnectionData: ServerConnectionData): Result<LoginSuccess> {
        val result = serverRepository.login(serverConnectionData)
        if (result.isSuccess) {
            userDataRepository.saveServerConnectionData(serverConnectionData)
        }
        return result
    }
}
