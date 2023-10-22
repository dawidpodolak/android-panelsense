package com.panelsense.data.repository

import com.panelsense.domain.model.ServerConnectionData
import com.panelsense.domain.repository.UserDataRepository
import javax.inject.Inject

class StorageUserDataRepository @Inject constructor() : UserDataRepository {
    override suspend fun isUserLoggedIn(): Boolean {
        return false
    }

    override suspend fun saveServerConnectionData(serverConnectionData: ServerConnectionData) {
        return Unit
    }
}
