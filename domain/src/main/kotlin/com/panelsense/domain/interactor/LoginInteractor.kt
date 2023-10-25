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

    suspend fun login(serverConnectionData: ServerConnectionData): Result<LoginSuccess> {
        val result = serverRepository.login(serverConnectionData)
        if (result.isSuccess) {
            userDataRepository.saveServerConnectionData(serverConnectionData)
        }
        return result
    }

    suspend fun relogin(): Result<LoginSuccess> {
        val serverConnectionData =
            userDataRepository.getServerConnectionData() ?: return Result.failure(
                IllegalStateException("No server connection data")
            )
        return serverRepository.login(serverConnectionData)
    }

    suspend fun logout() {
        userDataRepository.clearData()
    }
}
