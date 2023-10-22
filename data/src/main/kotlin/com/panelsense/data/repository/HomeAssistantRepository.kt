package com.panelsense.data.repository

import com.panelsense.domain.model.LoginSuccess
import com.panelsense.domain.model.ServerConnectionData
import com.panelsense.domain.repository.ServerRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class HomeAssistantRepository @Inject constructor() : ServerRepository {

    @Suppress("MagicNumber")
    override suspend fun login(serverConnectionData: ServerConnectionData): Result<LoginSuccess> {
        delay(2000)
        return Result.failure(Exception("Login failed"))
    }
}
