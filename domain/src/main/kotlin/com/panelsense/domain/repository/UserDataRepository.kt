package com.panelsense.domain.repository

import com.panelsense.domain.model.ServerConnectionData

interface UserDataRepository {
    suspend fun isUserLoggedIn(): Boolean
    suspend fun saveServerConnectionData(serverConnectionData: ServerConnectionData)
}
