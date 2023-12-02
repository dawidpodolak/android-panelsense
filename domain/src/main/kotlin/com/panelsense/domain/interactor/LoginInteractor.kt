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
    suspend fun isUserLoggedIn(): Boolean = userDataRepository.getServerConnectionData() != null

    suspend fun getServerConnectionData(): ServerConnectionData? =
        userDataRepository.getServerConnectionData()


    suspend fun login(serverConnectionData: ServerConnectionData): Result<LoginSuccess> {
        val result = serverRepository.login(serverConnectionData)
        if (result.isSuccess) {
            userDataRepository.saveServerConnectionData(serverConnectionData)
            serverRepository.requestEntitiesState(true)
        }
        return result
    }

    suspend fun relogin(): Result<LoginSuccess> {
        val serverConnectionData =
            userDataRepository.getServerConnectionData() ?: return Result.failure(
                IllegalStateException("No server connection data")
            )
        val loginResult = serverRepository.login(serverConnectionData)
        if (loginResult.isSuccess) {
            serverRepository.requestEntitiesState(true)
        }
        return loginResult
    }

    suspend fun logout() {
        userDataRepository.clearData()
    }
}
