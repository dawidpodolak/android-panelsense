package com.panelsense.domain.repository

import com.panelsense.domain.model.ServerConnectionData

interface UserDataRepository {
    suspend fun getServerConnectionData(): ServerConnectionData?
    suspend fun saveServerConnectionData(serverConnectionData: ServerConnectionData)
    fun clearData()
}
