package com.panelsense.domain.repository

import com.panelsense.domain.model.LoginSuccess
import com.panelsense.domain.model.ServerConnectionData

interface ServerRepository {
    suspend fun login(serverConnectionData: ServerConnectionData): Result<LoginSuccess>
}
